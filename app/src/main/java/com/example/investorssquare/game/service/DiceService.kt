package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game.diceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object DiceService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    fun rollDice(){
        diceViewModel.rollDice()
        serviceScope.launch { EventBus.postEvent(Event.ON_DICE_THROWN(diceViewModel.diceNumber1.value, diceViewModel.diceNumber2.value)) }
    }
    fun handleDiceThrown(firstNumber: Int, secondNumber: Int) {
        if(firstNumber==secondNumber){
            val player = Game.getActivePlayer()!!
            player.doublesRolledCounter++
            if(Game.ruleBook.playAgainIfRolledDouble){
                return
            }
        }
        disableDice()
    }
    fun disableDice(){
        diceViewModel.disableDiceButton()
        Game.showFinishButton()
    }
}