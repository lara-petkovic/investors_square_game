package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.domain.model.Tax
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

object TransactionService {
    var priceMultiplier = 1
    fun collectSalary() {
        val player = Game.getActivePlayer()!!
        player.receive(Game.ruleBook.salary)
    }
    fun receive(player: PlayerViewModel, money: Int){
        player.receive(money)
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
        if((receiver.isInJail.value && !Game.ruleBook.collectRentsWhileInJail)
            || estate.isMortgaged.value) return //dont pay if the estate is mortgaged or the owner is in jail
        if(payer!=receiver){
            val moneyToTransfer = calculateRentPrice(estate)
            Game.showPaymentPopup(payer, receiver, moneyToTransfer) {
                payer.pay(moneyToTransfer)
                receiver.receive(moneyToTransfer)
            }
        }
    }
    fun collectGatheredTaxes(){
        if(Game.gatheredTaxes.value>0){
            val player = Game.getActivePlayer()!!
            player.receive(Game.gatheredTaxes.value)
            Game.resetGatheredTaxes()
        }
    }
    fun payTax(){
        val player = Game.getActivePlayer()!!
        val field : Tax = Game.board.value?.fields?.get(player.position.value)!! as Tax
        val tax = if(Game.ruleBook.payingTaxesViaPercentagesEnabled && field.taxPercentage>0) minOf(field.tax, player.money.value * field.taxPercentage / 100) else field.tax
        player.pay(tax)
        if(Game.ruleBook.gatheringTaxesEnabled)
            Game.addToGatheredTaxes(tax)
    }
    fun pay(player: PlayerViewModel, money: Int){
        player.pay(money)
    }
    private fun calculateRentPrice(estate: EstateViewModel): Int{
        var ret = 0
        if(estate.isProperty){
            if(Game.ruleBook.doubleRentOnCollectedSetsEnabled
                && estate.numberOfBuildings.value==0
                && Game.doesPlayerOwnASet(estate.ownerIndex.value,(estate.estate as Property).setColor)
                ){
                ret = estate.estate.rent[0] * 2
            }
            else {
                ret = estate.estate.rent[estate.numberOfBuildings.value]
            }
        }
        if(estate.isUtility){
            val utilitiesOwned = Game.estates.value.filter { e -> e.isUtility && e.ownerIndex.value == estate.ownerIndex.value && !e.isMortgaged.value }.size
            val diceMultiplier = Game.diceViewModel.getDiceSum()
            ret = estate.estate.rent[utilitiesOwned - 1] * diceMultiplier
        }
        if(estate.isStation){
            val stationsOwned = Game.estates.value.filter { e -> e.isStation && e.ownerIndex.value == estate.ownerIndex.value && !e.isMortgaged.value }.size
            ret = estate.estate.rent[stationsOwned - 1]
        }
        ret *= priceMultiplier
        priceMultiplier = 1
        return ret
    }
    fun payGeneralRepairsOnBuildings(pricePerHouse: Int, pricePerHotel: Int){
        val player = Game.getActivePlayer()!!
        val totalPriceForHouses = pricePerHouse * EstateService.getNumberOfHousesOwned(player)
        val totalPriceForHotels = pricePerHotel * EstateService.getNumberOfHotelsOwned(player)
        player.pay(totalPriceForHouses + totalPriceForHotels)
    }
    fun payIfAffordable(player: PlayerViewModel, price: Int): Boolean{
        if(player.money.value<price)
            return false
        pay(player, price)
        return true
    }
}