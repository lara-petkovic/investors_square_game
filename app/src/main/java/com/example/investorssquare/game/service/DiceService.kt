package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game.board
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game.diceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object DiceService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.DiceThrown -> handleDiceThrown(event.firstNumber, event.secondNumber)
                    else -> { }
                }
            }
        }
    }
    private fun handleDiceThrown(firstNumber: Int, secondNumber: Int) {
        if(firstNumber!=secondNumber || Game.board.value?.ruleBook?.playAgainIfRolledDouble==false){
            diceViewModel.disableDiceButton()
            Game.showFinishButton()
        }
    }
}