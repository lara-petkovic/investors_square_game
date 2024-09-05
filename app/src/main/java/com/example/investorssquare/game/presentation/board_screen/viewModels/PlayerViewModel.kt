package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> get() = _players

    private val _activePlayerIndex = MutableStateFlow(0)
    val activePlayerIndex: StateFlow<Int> get() = _activePlayerIndex

    private val _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> get() = _timerRunning

    private val _diceNumber1 = MutableStateFlow(1)
    val diceNumber1: StateFlow<Int> get() = _diceNumber1

    private val _diceNumber2 = MutableStateFlow(1)
    val diceNumber2: StateFlow<Int> get() = _diceNumber2

    private val _isDiceButtonEnabled = MutableStateFlow(true)
    val isDiceButtonEnabled: StateFlow<Boolean> get() = _isDiceButtonEnabled

    private val _isFinishButtonVisible = MutableStateFlow(false)
    val isFinishButtonVisible: StateFlow<Boolean> get() = _isFinishButtonVisible

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                _timerRunning.value = true
                delay(5000) // 5 seconds
                switchToNextPlayer()
                _timerRunning.value = false
                delay(500) // Small delay to avoid immediate re-trigger
            }
        }
    }

    private fun switchToNextPlayer() {
        val currentIndex = _activePlayerIndex.value
        val nextIndex = (currentIndex + 1) % (_players.value.size.takeIf { it > 0 } ?: 1)
        _activePlayerIndex.value = nextIndex
    }

    fun setPlayers(players: List<Player>) {
        _players.value = players
    }

    fun rollDice() {
        _diceNumber1.value = Random.nextInt(1, 7)
        _diceNumber2.value = Random.nextInt(1, 7)

        _isDiceButtonEnabled.value = false // Disable dice button after roll
        _isFinishButtonVisible.value = true // Show finish button
    }

    fun finishTurn() {
        _isDiceButtonEnabled.value = true // Re-enable dice button
        _isFinishButtonVisible.value = false // Hide finish button
    }
}
