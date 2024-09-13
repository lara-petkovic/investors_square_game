package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor() : ViewModel() {

    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> get() = _board

    private val _estates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val estates: StateFlow<List<EstateViewModel>> get() = _estates

    private val _players = MutableStateFlow<List<PlayerViewModel>>(emptyList())
    val players: StateFlow<List<PlayerViewModel>> get() = _players

    val diceViewModel = DiceViewModel()

    private val _isFinishButtonVisible = MutableStateFlow(false)
    val isFinishButtonVisible: StateFlow<Boolean> get() = _isFinishButtonVisible

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> get() = _showPopup

    private val _currentField = MutableStateFlow<Field?>(null)
    val currentField: StateFlow<Field?> get() = _currentField

    private val _showPaymentPopup = MutableStateFlow(false)
    val showPaymentPopup: StateFlow<Boolean> = _showPaymentPopup

    private val _paymentDetails = MutableStateFlow<PaymentDetails?>(null)
    val paymentDetails: StateFlow<PaymentDetails?> = _paymentDetails

    @OptIn(ExperimentalCoroutinesApi::class)
    val playersPositions: StateFlow<List<Int>> = _players.flatMapLatest { players ->
        combine(players.map { it.position }) { positions ->
            positions.toList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun dismissPopup() {
        _showPopup.value = false
        _currentField.value = null
    }

    fun dismissPaymentPopup() {
        _showPaymentPopup.value = false
        _paymentDetails.value = null
    }

    fun setPlayers(playerNames: List<String>, playerColors: List<Color>, money: Int) {
        _players.value = playerNames.mapIndexed { index, name ->
            PlayerViewModel().apply {
                setMoney(money)
                setName(name)
                setColor(playerColors[index])
                setIndex(index)
            }
        }
        _players.value.firstOrNull()?.startMove()
    }

    fun rollDice() {
        diceViewModel.rollDice()
        _isFinishButtonVisible.value = true
    }

    fun moveActivePlayer() {
        getActivePlayer()?.let { player ->
            player.moveBySteps(diceViewModel.getDiceSum())
            player.position.value?.let { handleLandingPosition(player, it) }
        }
    }

    fun buyEstate(estateFieldIndex: Int) {
        getActivePlayer()?.let { player ->
            getEstateByFieldIndex(estateFieldIndex)?.let { estate ->
                player.buyNewEstate(estate)
                dismissPopup()
            }
        }
    }

    fun finishTurn() {
        diceViewModel.enableDiceButton()
        _isFinishButtonVisible.value = false
        switchToNextPlayer()
    }

    fun setBoard(board: Board) {
        _board.value = board
        _estates.value = board.fields.filterIsInstance<Estate>().map { EstateViewModel(it) }
    }

    fun getOwnerOfEstate(fieldIndex: Int): PlayerViewModel? {
        val ownerIndex = getEstateByFieldIndex(fieldIndex)?.ownerIndex?.value ?: return null
        return players.value.getOrNull(ownerIndex).takeIf { ownerIndex != -1 }
    }

    private fun getEstateByFieldIndex(index: Int): EstateViewModel? {
        return _estates.value.firstOrNull { it.estate.value.index == index }
    }

    private fun showPaymentPopup(payer: PlayerViewModel, receiver: PlayerViewModel, amount: Int) {
        _paymentDetails.value = PaymentDetails(payer, receiver, amount)
        _showPaymentPopup.value = true
        viewModelScope.launch {
            delay(3000)
            dismissPaymentPopup()
        }
    }

    private fun switchToNextPlayer() {
        val currentIndex = _players.value.indexOfFirst { it.isActive.value }
        if (currentIndex != -1) {
            _players.value[currentIndex].finishMove()
            val nextIndex = (currentIndex + 1) % _players.value.size
            _players.value[nextIndex].startMove()
        }
    }

    private fun getActivePlayer(): PlayerViewModel? {
        return _players.value.firstOrNull { it.isActive.value }
    }

    private fun handleLandingPosition(player: PlayerViewModel, position: Int) {
        getEstateByFieldIndex(position)?.let { estate ->
            if (estate.ownerIndex.value != -1) {
                val moneyToTransfer = estate.estate.value.rent[0]
                player.pay(moneyToTransfer)
                players.value[estate.ownerIndex.value].receive(moneyToTransfer)
                showPaymentPopup(player, players.value[estate.ownerIndex.value], moneyToTransfer)
            } else {
                showPopupForField(position)
            }
        } ?: showPopupForField(position)
    }

    private fun showPopupForField(fieldIndex: Int) {
        _currentField.value = _board.value?.fields?.get(fieldIndex)
        _showPopup.value = true
    }
}

data class PaymentDetails(
    val payer: PlayerViewModel,
    val receiver: PlayerViewModel,
    val amount: Int
)