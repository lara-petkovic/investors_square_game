package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game.diceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object DiceService {
    fun handleDiceThrown(firstNumber: Int, secondNumber: Int) {
        if(firstNumber!=secondNumber || !Game.ruleBook.playAgainIfRolledDouble){
            disableDice()
        }
    }
    fun disableDice(){
        diceViewModel.disableDiceButton()
        Game.showFinishButton()
    }
}