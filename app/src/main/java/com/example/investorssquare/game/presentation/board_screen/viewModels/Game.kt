//package com.example.investorssquare.game.presentation.board_screen.viewModels
//
//import androidx.compose.ui.graphics.Color
//import com.example.investorssquare.game.domain.model.Board
//import com.example.investorssquare.game.domain.model.Estate
//import com.example.investorssquare.game.domain.model.Field
//import com.example.investorssquare.game.domain.model.Property
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//object Game {
// TODO   private val gameScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
//
//    private val _board = MutableStateFlow<Board?>(null)
//    val board: StateFlow<Board?> get() = _board
//
//    private val _estates = MutableStateFlow<List<EstateViewModel>>(emptyList())
//    val estates: StateFlow<List<EstateViewModel>> get() = _estates
//
//   private val _players = MutableStateFlow<List<PlayerViewModel>>(emptyList())
//    val players: StateFlow<List<PlayerViewModel>> get() = _players
//
//    val diceViewModel = DiceViewModel()
//
//    private val _isFinishButtonVisible = MutableStateFlow(false)
//    val isFinishButtonVisible: StateFlow<Boolean> get() = _isFinishButtonVisible
//
//
//    private val _gatheredTaxes = MutableStateFlow(0)
//    val gatheredTaxes: StateFlow<Int> get() = _gatheredTaxes
//
//    private val _showPopup = MutableStateFlow(false)
//    val showPopup: StateFlow<Boolean> get() = _showPopup
//
//    private val _currentField = MutableStateFlow<Field?>(null)
//    val currentField: StateFlow<Field?> get() = _currentField
//
//    private val _showPaymentPopup = MutableStateFlow(false)
//    val showPaymentPopup: StateFlow<Boolean> = _showPaymentPopup
//
//    private val _paymentDetails = MutableStateFlow<PaymentDetails?>(null)
//    val paymentDetails: StateFlow<PaymentDetails?> = _paymentDetails
//
//    private val _highlightMode = MutableStateFlow<Boolean>(false)
//    val highlightMode: StateFlow<Boolean> = _highlightMode
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val playersPositions: StateFlow<List<Int>> = _players.flatMapLatest { players ->
//        combine(players.map { it.position }) { positions ->
//            positions.toList()
//        }
//    }.stateIn(
//        scope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
//        started = SharingStarted.Eagerly,
//        initialValue = emptyList()
//    )
//
//    fun dismissPopup() {
//        _showPopup.value = false
//    }
//
//    fun dismissPaymentPopup() {
//        _showPaymentPopup.value = false
//    }
//
//    fun resetGatheredTaxes(){
//        _gatheredTaxes.value = 0
//    }
//
//    fun addToGatheredTaxes(amount: Int){
//        _gatheredTaxes.value += amount
//    }
//
//    fun showPopupForField(fieldIndex: Int) {
//        _currentField.value = _board.value?.fields?.get(fieldIndex)
//        _showPopup.value = true
//    }
//
//    private fun getAllPropertiesBySet(set: Color): List<EstateViewModel> {
//        return estates.value.filter{e -> e.isProperty && (e.estate as Property).setColor == set }
//    }
//
//    fun doesPlayerOwnASet(ownerIndex: Int, set: Color): Boolean {
//        return getAllPropertiesBySet(set).all{e -> e.ownerIndex.value == ownerIndex}
//    }
//
//    fun setPlayers(playerNames: List<String>, playerColors: List<Color>, money: Int) {
//        _players.value = playerNames.mapIndexed { index, name ->
//            PlayerViewModel().apply {
//                setMoney(money)
//                setName(name)
//                setColor(playerColors[index])
//                setIndex(index)
//            }
//        }
//        _players.value.firstOrNull()?.startMove()
//    }
//
//    fun showFinishButton() {
//        _isFinishButtonVisible.value = true
//    }
//
// fun getPropertiesBySetColor(set: Color): List<EstateViewModel>{
//        return estates.value.filter { e -> e.isProperty && (e.estate as Property).setColor == set }
//    }
//
//    fun hideFinishButton() {
//        _isFinishButtonVisible.value = false
//    }

//    fun setBoard(board: Board) {
//        _board.value = board
//        _estates.value = board.fields.filterIsInstance<Estate>().map { EstateViewModel(it) }
//    }
//
//   fun getOwnerOfEstate(fieldIndex: Int): PlayerViewModel? {
//        val ownerIndex = getEstateByFieldIndex(fieldIndex)?.ownerIndex?.value
//        return players.value.getOrNull(ownerIndex ?: -1).takeIf { ownerIndex != -1 }
//    }
//
//   fun getEstateByFieldIndex(index: Int): EstateViewModel? {
//        return _estates.value.firstOrNull { it.estate.index == index }
//    }
//
//    fun showPaymentPopup(
//        payer: PlayerViewModel,
//        receiver: PlayerViewModel,
//        amount: Int,
//        onDismissAction: () -> Unit
//    ) {
//        _paymentDetails.value = PaymentDetails(payer, receiver, amount)
//        _showPaymentPopup.value = true
//        gameScope.launch {
//            delay(1000)
//            dismissPaymentPopup()
//            onDismissAction()
//        }
//    }
//
//  fun getActivePlayer(): PlayerViewModel? {
//        return _players.value.firstOrNull { it.isActive.value }
//    }

//    fun turnOnHighlightMode(){
//        _highlightMode.value = true
//    }
//    fun turnOffHighlightMode(){
//        _highlightMode.value = false
//    }
//}
//
// data class PaymentDetails(
//    val payer: PlayerViewModel,
//    val receiver: PlayerViewModel,
//    val amount: Int
//)