package com.example.investorssquare.game.presentation.board_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> get() = _players

    private val _activePlayerIndex = MutableStateFlow(0)
    val activePlayerIndex: StateFlow<Int> get() = _activePlayerIndex

    private val _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> get() = _timerRunning

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
}
