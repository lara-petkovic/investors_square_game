package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.EstateService.getEstateByFieldIndex
import com.example.investorssquare.game.service.PlayerMovementService.moveStepByStepBackwards
import com.example.investorssquare.game.service.PlayerMovementService.moveStepByStepForward
import com.example.investorssquare.game.service.PlayerMovementService.moveToField
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.PlayersService.players
import com.example.investorssquare.game.service.TransactionService.pay
import com.example.investorssquare.game.service.TransactionService.payGeneralRepairsOnBuildings
import com.example.investorssquare.game.service.TransactionService.priceMultiplier
import com.example.investorssquare.game.service.TransactionService.receive
import com.example.investorssquare.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object CommunityCardActionsService {
    fun action1(){
        moveToField(39)
    }
    fun action2(){
        moveToField(0)
    }
    fun action3(){
        moveToField(24)
    }
    fun action4(){
        moveToField(11)
    }
    fun action5(){
        val player = getActivePlayer()!!
        var position = player.position.value
        var steps = 0
        while(true){
            if(board.value?.fields?.get(position)?.type==FieldType.STATION){
                val stationOwner = getEstateByFieldIndex(position)?.ownerIndex?.value!!
                if(stationOwner != -1 && stationOwner != player.index.value){
                    priceMultiplier = 2
                }
                moveStepByStepForward(steps, player, 80)
                break
            }
            position = (position + 1)% Constants.TOTAL_FIELDS
            steps++
        }
    }
    fun action6(){
        action5()
    }
    fun action7(){
        val player = getActivePlayer()!!
        var position = player.position.value
        var steps = 0
        while(true){
            if(board.value?.fields?.get(position)?.type==FieldType.UTILITY){
                var utility = getEstateByFieldIndex(position)!!
                val utilityOwner = utility.ownerIndex.value
                if(utilityOwner != -1 && utilityOwner != player.index.value){
                    val utilitiesOwned = estates.value.filter { e -> e.isUtility && e.ownerIndex.value == utilityOwner }.size
                    priceMultiplier = 10/utility.estate.rent[utilitiesOwned - 1]
                }
                moveStepByStepForward(steps, player, 80)
                break
            }
            position = (position + 1)% Constants.TOTAL_FIELDS
            steps++
        }
    }
    fun action8(){
        val player = getActivePlayer()!!
        receive(player, 50)
    }
    fun action9(){
        val player = getActivePlayer()!!
        player.acquireGetOutOfJailFreeCard()
    }
    fun action10(){
        val player = getActivePlayer()!!
        moveStepByStepBackwards(3, player)
    }
    fun action11(){
        val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        serviceScope.launch { EventBus.postEvent(Event.ON_GO_TO_JAIL) }
    }
    fun action12(){
        payGeneralRepairsOnBuildings(25,100)
    }
    fun action13(){
        val player = getActivePlayer()!!
        pay(player, 15)
    }
    fun action14(){
        moveToField(5)
    }
    fun action15(){
        val player = getActivePlayer()!!
        for(p in players.value){
            receive(p,50)
        }
        pay(player, players.value.size*50)
    }
    fun action16(){
        val player = getActivePlayer()!!
        receive(player, 150)
    }
    fun action17(){
        action2()
    }
    fun action18(){
        val player = getActivePlayer()!!
        receive(player, 200)
    }
    fun action19(){
        val player = getActivePlayer()!!
        pay(player, 50)
    }
    fun action20(){
        val player = getActivePlayer()!!
        receive(player, 50)
    }
    fun action21(){
        action9()
    }
    fun action22(){
        action11()
    }
    fun action23(){
        val player = getActivePlayer()!!
        receive(player, 100)
    }
    fun action24(){
        val player = getActivePlayer()!!
        receive(player,20)
    }
    fun action25(){
        val player = getActivePlayer()!!
        for(p in players.value){
            pay(p,10)
        }
        receive(player, 10 * players.value.size)
    }
    fun action26(){
        val player = getActivePlayer()!!
        receive(player, 100)
    }
    fun action27(){
        val player = getActivePlayer()!!
        pay(player,100)
    }
    fun action28(){
        val player = getActivePlayer()!!
        pay(player,150)
    }
    fun action29(){
        val player = getActivePlayer()!!
        receive(player,25)
    }
    fun action30(){
        payGeneralRepairsOnBuildings(40,115)
    }
    fun action31(){
        val player = getActivePlayer()!!
        receive(player,10)
    }
    fun action32(){
        val player = getActivePlayer()!!
        receive(player,100)
    }
}