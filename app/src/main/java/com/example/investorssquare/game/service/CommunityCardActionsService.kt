package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object CommunityCardActionsService {
    fun action1(){
        PlayerMovementService.moveToField(39)
    }
    fun action2(){
        PlayerMovementService.moveToField(0)
    }
    fun action3(){
        PlayerMovementService.moveToField(24)
    }
    fun action4(){
        PlayerMovementService.moveToField(11)
    }
    fun action5(){
        val player = Game.getActivePlayer()!!
        var position = player.position.value
        var steps = 0
        while(true){
            if(Game.board.value?.fields?.get(position)?.type==FieldType.STATION){
                val stationOwner = Game.getEstateByFieldIndex(position)?.ownerIndex?.value!!
                if(stationOwner != -1 && stationOwner != player.index.value){
                    TransactionService.priceMultiplier = 2
                }
                PlayerMovementService.moveStepByStepForward(steps, player, 80)
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
        val player = Game.getActivePlayer()!!
        var position = player.position.value
        var steps = 0
        while(true){
            if(Game.board.value?.fields?.get(position)?.type==FieldType.UTILITY){
                var utility = Game.getEstateByFieldIndex(position)!!
                val utilityOwner = utility.ownerIndex.value
                if(utilityOwner != -1 && utilityOwner != player.index.value){
                    val utilitiesOwned = Game.estates.value.filter { e -> e.isUtility && e.ownerIndex.value == utilityOwner }.size
                    TransactionService.priceMultiplier = 10/utility.estate.rent[utilitiesOwned - 1]
                }
                PlayerMovementService.moveStepByStepForward(steps, player, 80)
                break
            }
            position = (position + 1)% Constants.TOTAL_FIELDS
            steps++
        }
    }
    fun action8(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 50)
    }
    fun action9(){
        val player = Game.getActivePlayer()!!
        player.acquireGetOutOfJailFreeCard()
    }
    fun action10(){
        val player = Game.getActivePlayer()!!
        PlayerMovementService.moveStepByStepBackwards(3, player)
    }
    fun action11(){
        val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        serviceScope.launch { EventBus.postEvent(Event.ON_GO_TO_JAIL) }
    }
    fun action12(){
        TransactionService.payGeneralRepairsOnBuildings(25,100)
    }
    fun action13(){
        val player = Game.getActivePlayer()!!
        TransactionService.pay(player, 15)
    }
    fun action14(){
        PlayerMovementService.moveToField(5)
    }
    fun action15(){
        val player = Game.getActivePlayer()!!
        for(p in Game.players.value){
            TransactionService.receive(p,50)
        }
        TransactionService.pay(player, Game.players.value.size*50)
    }
    fun action16(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 150)
    }
    fun action17(){
        action2()
    }
    fun action18(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 200)
    }
    fun action19(){
        val player = Game.getActivePlayer()!!
        TransactionService.pay(player, 50)
    }
    fun action20(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 50)
    }
    fun action21(){
        action9()
    }
    fun action22(){
        action11()
    }
    fun action23(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 100)
    }
    fun action24(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player,20)
    }
    fun action25(){
        val player = Game.getActivePlayer()!!
        for(p in Game.players.value){
            TransactionService.pay(p,10)
        }
        TransactionService.receive(player, 10 * Game.players.value.size)
    }
    fun action26(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, 100)
    }
    fun action27(){
        val player = Game.getActivePlayer()!!
        TransactionService.pay(player,100)
    }
    fun action28(){
        val player = Game.getActivePlayer()!!
        TransactionService.pay(player,150)
    }
    fun action29(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player,25)
    }
    fun action30(){
        TransactionService.payGeneralRepairsOnBuildings(40,115)
    }
    fun action31(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player,10)
    }
    fun action32(){
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player,100)
    }
}