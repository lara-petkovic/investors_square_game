package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.domain.model.Tax
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object TransactionService {
    fun collectSalary() {
        val player = Game.getActivePlayer()!!
        player.receive(Game.ruleBook.salary)
    }
    fun payPriceForEstate(index: Int) : Boolean{
        val player = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(index)!!
        if(player.money.value>=estate.estate.price){
            player.pay(estate.estate.price)
            return true
        }
        return false
    }
    fun payRent() {
        val payer = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(payer.position.value)!!
        val receiver = Game.getOwnerOfEstate(estate.estate.index)!!
        if(payer!=receiver){
            val moneyToTransfer = calculatePrice(estate)
            Game.showPaymentPopup(payer, receiver, moneyToTransfer) {
                payer.pay(moneyToTransfer)
                receiver.receive(moneyToTransfer)
            }
        }
    }
    fun payTax(){
        val player = Game.getActivePlayer()!!
        val field : Tax = Game.board.value?.fields?.get(player.position.value)!! as Tax
        player.pay(field.tax)
    }
    private fun calculatePrice(estate: EstateViewModel): Int{
        if(estate.isProperty){
            if(Game.ruleBook.doubleRentOnCollectedSetsEnabled
                && estate.numberOfBuildings.value==0
                && Game.doesPlayerOwnASet(estate.ownerIndex.value,(estate.estate as Property).setColor)
                ){
                return estate.estate.rent[0] * 2
            }
            return estate.estate.rent[estate.numberOfBuildings.value]
        }
        if(estate.isUtility){
            val utilitiesOwned = Game.estates.value.filter { e -> e.isUtility && e.ownerIndex.value == estate.ownerIndex.value }.size
            val diceMultiplier = Game.diceViewModel.getDiceSum()
            return estate.estate.rent[utilitiesOwned - 1] * diceMultiplier
        }
        if(estate.isStation){
            val stationsOwned = Game.estates.value.filter { e -> e.isStation && e.ownerIndex.value == estate.ownerIndex.value }.size
            return estate.estate.rent[stationsOwned - 1]
        }
        return 0
    }
}