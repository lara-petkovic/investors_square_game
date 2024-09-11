package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor() : ViewModel() {

    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> get() = _board

    private val _estates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val estates : StateFlow<List<EstateViewModel>> get() = _estates

    private val _players = MutableStateFlow<List<PlayerViewModel>>(emptyList())
    val players: StateFlow<List<PlayerViewModel>> get() = _players

    var diceViewModel: DiceViewModel = DiceViewModel()

    @OptIn(ExperimentalCoroutinesApi::class)
    val playersPositions: StateFlow<List<Int>> = combine(
        _players,
        _players.flatMapLatest { players ->
            combine(players.map { it.position }) { positions -> positions.toList() }
        }
    ) { _, positions ->
        positions
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val _isFinishButtonVisible = MutableStateFlow(false)
    val isFinishButtonVisible: StateFlow<Boolean> get() = _isFinishButtonVisible

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> get() = _showPopup

    private val _currentField = MutableStateFlow<Field?>(null)
    val currentField: StateFlow<Field?> get() = _currentField

    private fun switchToNextPlayer() {
        for(i in 0..<_players.value.size){
            if(_players.value[i].isActive.value){
                _players.value[i].finishMove()
                _players.value[if(i<_players.value.size-1) i+1 else 0].startMove()
                break
            }
        }
    }
    private fun getActivePlayer(): PlayerViewModel? {
        for(player in _players.value){
            if(player.isActive.value){
                return player
            }
        }
        return null
    }
    fun setPlayers(playerNames: List<String>, playerColors: List<Color>, money: Int) {
        _players.value = List(playerNames.size) { PlayerViewModel() }
        for(i in 0..<_players.value.size){
            _players.value[i].setMoney(money)
            _players.value[i].setName(playerNames[i])
            _players.value[i].setColor(playerColors[i])
            _players.value[i].setIndex(i)
        }
        _players.value[0].startMove()
    }
    fun rollDice() {
        diceViewModel.rollDice()
        _isFinishButtonVisible.value = true
    }
    fun moveActivePlayer() {
        val activePlayer = getActivePlayer()
        activePlayer?.moveBySteps(diceViewModel.getDiceSum())
        activePlayer?.position?.value?.let { handleLandingPosition(activePlayer, it) }
    }
    fun buyEstate(estateFieldIndex: Int){
        val player = getActivePlayer()
        val estate = getEstateByFieldIndex(estateFieldIndex)
        estate?.let { player?.buyNewEstate(estate) }
        dismissPopup()
    }
    private fun handleLandingPosition(player: PlayerViewModel, position: Int){
        val estate = getEstateByFieldIndex(position)
        if(estate!=null && estate.ownerIndex.value!=-1){
            val moneyToTransfer = estate.estate.value.rent[0]
            player.pay(moneyToTransfer)
            _players.value[estate.ownerIndex.value].receive(moneyToTransfer)
        }
        else{
            showPopupForField(position)
        }
    }
    fun finishTurn() {
        diceViewModel.enableDiceButton()
        _isFinishButtonVisible.value = false
        switchToNextPlayer()
    }
    fun setBoard(board: Board){
        _board.value = board
        val estateFields = board.fields.filterIsInstance<Estate>()
        _estates.value = estateFields.map { estate ->
            EstateViewModel(estate)
        }
    }
    fun getOwnerOfEstate(fieldIndex: Int): PlayerViewModel?{
        val ownerIndex = getEstateByFieldIndex(fieldIndex)?.ownerIndex?.value!!
        if(ownerIndex==-1)
            return null
        return players.value[ownerIndex]
    }
    fun getEstateByFieldIndex(index: Int): EstateViewModel?{
        return _estates.value.firstOrNull(){ estate ->
            estate.estate.value.index==index
        }
    }
    fun dismissPopup(){
        _showPopup.value = false
        _currentField.value = null
    }
    private fun showPopupForField(fieldIndex: Int){
        _currentField.value = _board.value?.fields?.get(fieldIndex)
        _showPopup.value = true
    }
}
