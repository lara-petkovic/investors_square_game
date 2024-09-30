package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object PlayerMovementService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun moveActivePlayer() {
        Game.getActivePlayer()?.let { player ->
            if(player.doublesRolledCounter==Game.ruleBook.doublesRolledLimit){
                serviceScope.launch { EventBus.postEvent(Event.ON_GO_TO_JAIL) }
                return
            }
            val diceSum = Game.diceViewModel.getDiceSum()
            moveStepByStepForward(diceSum, player)
        }
    }

    private fun moveStepByStepForward(steps: Int, player: PlayerViewModel) = serviceScope.launch {
        for (i in 1..steps) {
            player.moveBySteps(1)
            if (player.position.value == 0) {
                serviceScope.launch { EventBus.postEvent(Event.ON_PLAYER_CROSSED_START) }
            }
            delay(200)
        }
        handlePlayerLanding()
    }

    fun moveToField(fieldIndex: Int){
        Game.getActivePlayer()?.let { player ->
            moveStepByStepForward(fieldIndex - player.position.value, player)
        }
    }

    fun goToJail(){
        val player = Game.getActivePlayer()!!
        player.goToJail(Game.ruleBook.jailSentenceInMoves)
        val jailPosition = Game.board.value?.fields?.find { field-> field.type==FieldType.JAIL }?.index!!
        serviceScope.launch {
            while (player.position.value!=jailPosition) {
                if(player.position.value>jailPosition)
                    player.moveByStepsBackwards(1)
                else
                    player.moveBySteps(1)
                delay(70)
            }
        }
    }
    private fun handlePlayerLanding() {
        val position = Game.getActivePlayer()?.position?.value!!
        val field: Field = Game.board.value?.fields?.get(position)!!
        when(field.type){
            FieldType.GO_TO_JAIL -> {serviceScope.launch{EventBus.postEvent(Event.ON_GO_TO_JAIL)}}
            FieldType.CHANCE, FieldType.COMMUNITY_CHEST -> {serviceScope.launch{EventBus.postEvent(Event.ON_COMMUNITY_CARD_OPENED)}}
            FieldType.GO -> {}
            FieldType.JAIL -> {}
            FieldType.PARKING -> {serviceScope.launch{EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_FREE_PARKING)}}
            FieldType.TAX -> {serviceScope.launch{EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_TAX)}}
            else -> {
                serviceScope.launch {
                    if (Game.getOwnerOfEstate(position) != null)
                        EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE)
                    else
                        EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_FREE_ESTATE)
                }
            }
        }
    }
}
