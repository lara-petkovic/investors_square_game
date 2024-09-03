package com.example.investorssquare.game.presentation.board_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _activePlayerIndex = MutableLiveData(0)
    val activePlayerIndex: LiveData<Int> get() = _activePlayerIndex

    init {
        // Start a timer that switches the active player every 5 seconds
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _activePlayerIndex.value = (_activePlayerIndex.value!! + 1) % (_players.value?.size ?: 1)
            }
        }
    }

    fun setPlayers(players: List<Player>) {
        _players.value = players
    }
}