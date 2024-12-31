package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.DiceViewModel
import com.example.investorssquare.game.service.MoveService.hideFinishButton
import com.example.investorssquare.game.service.MoveService.showFinishButton
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.random.Random

object DiceService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val diceViewModel = DiceViewModel()

    fun rollDice(){
        diceViewModel.rollDice()
        serviceScope.launch { EventBus.postEvent(Event.ON_DICE_THROWN(diceViewModel.diceNumber1.value, diceViewModel.diceNumber2.value)) }
    }

    fun getDiceSum(): Int{
        return diceViewModel.getDiceSum()
    }

    fun isRolledDouble(): Boolean{
        return diceViewModel.isRolledDouble()
    }

    fun handleDiceThrown(firstNumber: Int, secondNumber: Int) {
        val player = getActivePlayer() ?: return
        if (firstNumber != secondNumber) {
            if (!player.isInJail.value)
                serviceScope.launch { EventBus.postEvent(Event.ON_MOVE_PLAYER) }
            disableDice()
            return
        }
        if (player.isInJail.value && RuleBook.rollADoubleToEscapeJailEnabled) {
            player.escapeJail()
            disableDice()
            serviceScope.launch { EventBus.postEvent(Event.ON_MOVE_PLAYER) }
            return
        }
        player.doublesRolledCounter++
        if (!RuleBook.playAgainIfRolledDouble) {
            disableDice()
        }
        serviceScope.launch { EventBus.postEvent(Event.ON_MOVE_PLAYER) }
    }

    fun disableDice(){
        diceViewModel.disableDiceButton()
        showFinishButton()
    }

    fun enableDice(){
        diceViewModel.enableDiceButton()
        hideFinishButton()
    }
}