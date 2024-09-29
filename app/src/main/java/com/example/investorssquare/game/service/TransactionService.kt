package com.example.investorssquare.game.service

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
    fun collectSalary(){
        val player = Game.getActivePlayer()!!
        player.receive(Game.board.value?.ruleBook?.salary!!)
    }
    fun payPriceForEstate(index: Int) : Boolean{
        val player = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(index)!!
        if(player.money.value>=estate.estate.value.price){
            player.pay(estate.estate.value.price)
            return true
        }
        return false
    }
    fun payRent() {
        val payer = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(payer.position.value)!!
        val receiver = Game.getOwnerOfEstate(estate.estate.value.index)!!
        if(payer!=receiver){
            val moneyToTransfer = calculatePrice(estate)
            payer.pay(moneyToTransfer)
            receiver.receive(moneyToTransfer)
            Game.showPaymentPopup(payer, receiver, moneyToTransfer)
        }
    }
    fun payTax(){
        val player = Game.getActivePlayer()!!
        val field : Tax = Game.board.value?.fields?.get(player.position.value)!! as Tax
        player.pay(field.tax)
    }
    private fun calculatePrice(estate: EstateViewModel): Int{
        if(estate.isProperty){
            return estate.estate.value.rent[estate.numberOfBuildings.value]
        }
        if(estate.isUtility){
            val utilitiesOwned = Game.estates.value.filter { e -> e.isUtility && e.ownerIndex.value == estate.ownerIndex.value }.size
            val diceMultiplier = Game.diceViewModel.getDiceSum()
            return estate.estate.value.rent[utilitiesOwned - 1] * diceMultiplier
        }
        if(estate.isStation){
            val stationsOwned = Game.estates.value.filter { e -> e.isStation && e.ownerIndex.value == estate.ownerIndex.value }.size
            return estate.estate.value.rent[stationsOwned - 1]
        }
        return 0
    }
}