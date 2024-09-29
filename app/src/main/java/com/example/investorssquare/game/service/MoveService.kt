package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MoveService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.ON_MOVE_FINISHED -> handleFinishedMove()
                    else -> { }
                }
            }
        }
    }
    private fun handleFinishedMove() {
        Game.diceViewModel.enableDiceButton()
        Game.hideFinishButton()
        val activePlayer = Game.getActivePlayer()!!
        activePlayer.finishMove()
        val nextPlayersIndex = (activePlayer.index.value+1) % Game.players.value.size
        Game.players.value[nextPlayersIndex].startMove()
    }
}