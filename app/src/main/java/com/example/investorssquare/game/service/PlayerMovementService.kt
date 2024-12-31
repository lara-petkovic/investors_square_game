package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.DiceService.getDiceSum
import com.example.investorssquare.game.service.EstateService.getOwnerOfEstate
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.PlayersService.players
import com.example.investorssquare.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object PlayerMovementService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    val playersPositions: StateFlow<List<Int>> = players.flatMapLatest { players ->
        combine(players.map { it.position }) { positions ->
            positions.toList()
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun moveActivePlayer() {
        getActivePlayer()?.let { player ->
            if(player.doublesRolledCounter == RuleBook.doublesRolledLimit){
                serviceScope.launch { EventBus.postEvent(Event.ON_GO_TO_JAIL) }
                return
            }
            val diceSum = getDiceSum()
            moveStepByStepForward(diceSum, player, 200)
        }
    }

    fun moveStepByStepForward(steps: Int, player: PlayerViewModel, delay: Long) = serviceScope.launch {
        for (i in 1..steps) {
            player.moveBySteps(1)
            if (player.position.value == 0) {
                serviceScope.launch { EventBus.postEvent(Event.ON_PLAYER_CROSSED_START) }
            }
            delay(delay)
        }
        handlePlayerLanding()
    }

    fun moveStepByStepBackwards(steps: Int, player: PlayerViewModel) = serviceScope.launch {
        for (i in 1..steps) {
            player.moveByStepsBackwards(1)
            delay(80)
        }
        handlePlayerLanding()
    }

    fun moveToField(fieldIndex: Int){
        getActivePlayer()?.let { player ->
            val steps = (fieldIndex + Constants.TOTAL_FIELDS - player.position.value) % Constants.TOTAL_FIELDS
            moveStepByStepForward(steps, player, 80)
        }
    }

    fun goToJail(){
        val player = getActivePlayer()!!
        player.goToJail(RuleBook.jailSentenceInMoves)
        val jailPosition = board.value?.fields?.find { field-> field.type==FieldType.JAIL }?.index!!
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
        val position = getActivePlayer()?.position?.value!!
        val field: Field = board.value?.fields?.get(position)!!
        when(field.type){
            FieldType.GO_TO_JAIL -> {serviceScope.launch{EventBus.postEvent(Event.ON_GO_TO_JAIL)}}
            FieldType.CHANCE, FieldType.COMMUNITY_CHEST -> {serviceScope.launch{EventBus.postEvent(Event.ON_COMMUNITY_CARD_OPENED)}}
            FieldType.GO -> {}
            FieldType.JAIL -> {}
            FieldType.PARKING -> {serviceScope.launch{EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_FREE_PARKING)}}
            FieldType.TAX -> {serviceScope.launch{EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_TAX)}}
            else -> {
                serviceScope.launch {
                    if (getOwnerOfEstate(position) != null)
                        EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE)
                    else
                        EventBus.postEvent(Event.ON_PLAYER_LANDED_ON_FREE_ESTATE)
                }
            }
        }
    }
}
