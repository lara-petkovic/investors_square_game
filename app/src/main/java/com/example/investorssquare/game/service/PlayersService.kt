package com.example.investorssquare.game.service

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PlayersService {
    private val _players = MutableStateFlow<List<PlayerViewModel>>(emptyList())
    val players: StateFlow<List<PlayerViewModel>> get() = _players

    fun getActivePlayer(): PlayerViewModel? {
        return _players.value.firstOrNull { it.isActive.value }
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
}