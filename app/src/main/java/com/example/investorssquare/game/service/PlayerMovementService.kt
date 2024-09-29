package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object PlayerMovementService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.DiceThrown -> moveActivePlayer()
                    is Event.PlayerLanded -> handlePlayerLanding()
                    else -> { }
                }
            }
        }
    }
    private fun moveActivePlayer() {
        Game.getActivePlayer()?.let { player ->
            val diceSum = Game.diceViewModel.getDiceSum()
            serviceScope.launch {
                for (i in 1..diceSum) {
                    player.moveBySteps(1)
                    if (player.position.value == 0) {
                        serviceScope.launch { EventBus.postEvent(Event.PlayerCrossedStart) }
                    }
                    delay(150)
                }
                serviceScope.launch { EventBus.postEvent(Event.PlayerLanded) }
            }
        }
    }
    private fun handlePlayerLanding() {
        Game.getEstateByFieldIndex(Game.getActivePlayer()?.position?.value ?: -1)?.let { estate ->
            val ownerIndex = estate.ownerIndex.value
            serviceScope.launch {
                if (ownerIndex != -1)
                    EventBus.postEvent(Event.PlayerLandedOnBoughtEstate)
                else
                    EventBus.postEvent(Event.PlayerLandedOnFreeEstate)
            }
        } ?: serviceScope.launch { EventBus.postEvent(Event.PlayerLandedOnFreeEstate) }
    }
}
