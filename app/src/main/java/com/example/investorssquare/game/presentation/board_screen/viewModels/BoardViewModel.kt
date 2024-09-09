package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    }
    fun finishTurn() {
        diceViewModel.enableDiceButton()
        _isFinishButtonVisible.value = false
        switchToNextPlayer()
    }
}
