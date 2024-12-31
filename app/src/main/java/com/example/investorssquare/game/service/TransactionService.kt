package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.domain.model.Tax
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.DiceService.getDiceSum
import com.example.investorssquare.game.service.EstateService.doesPlayerOwnASet
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.EstateService.getEstateByFieldIndex
import com.example.investorssquare.game.service.EstateService.getNumberOfHotelsOwned
import com.example.investorssquare.game.service.EstateService.getNumberOfHousesOwned
import com.example.investorssquare.game.service.EstateService.getOwnerOfEstate
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object TransactionService {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    var priceMultiplier = 1

    private val _gatheredTaxes = MutableStateFlow(0) //gathering taxes in the center of the board
    val gatheredTaxes: StateFlow<Int> get() = _gatheredTaxes

    private val _showPaymentPopup = MutableStateFlow(false) //payment popup
    val showPaymentPopup: StateFlow<Boolean> = _showPaymentPopup

    private val _paymentDetails = MutableStateFlow<PaymentDetails?>(null)
    val paymentDetails: StateFlow<PaymentDetails?> = _paymentDetails

    fun collectSalary() {
        val player = getActivePlayer()!!
        receive(player, RuleBook.salary)
    }

    fun receive(player: PlayerViewModel, money: Int){
        player.receive(money)
    }

    fun payPriceForEstate(index: Int) : Boolean{
        val player = getActivePlayer()!!
        val estate = getEstateByFieldIndex(index)!!
        if(player.money.value>=estate.estate.price){
            player.pay(estate.estate.price)
            return true
        }
        return false
    }

    fun payRent() {
        val payer = getActivePlayer()!!
        val estate = getEstateByFieldIndex(payer.position.value)!!
        val receiver = getOwnerOfEstate(estate.estate.index)!!
        if((receiver.isInJail.value && !RuleBook.collectRentsWhileInJail)
            || estate.isMortgaged.value) return //don't pay if the estate is mortgaged or the owner is in jail
        if(payer!=receiver){
            val moneyToTransfer = calculateRentPrice(estate)
            showPaymentPopup(payer, receiver, moneyToTransfer) {
                payer.pay(moneyToTransfer)
                receiver.receive(moneyToTransfer)
            }
        }
    }

    fun payTax(){
        val player = getActivePlayer()!!
        val field : Tax = board.value?.fields?.get(player.position.value)!! as Tax
        val tax = if(RuleBook.payingTaxesViaPercentagesEnabled && field.taxPercentage>0) minOf(field.tax, player.money.value * field.taxPercentage / 100) else field.tax
        player.pay(tax)
        if(RuleBook.gatheringTaxesEnabled)
            addToGatheredTaxes(tax)
    }

    fun pay(player: PlayerViewModel, money: Int){
        player.pay(money)
    }

    fun payGeneralRepairsOnBuildings(pricePerHouse: Int, pricePerHotel: Int){
        val player = getActivePlayer()!!
        val totalPriceForHouses = pricePerHouse * getNumberOfHousesOwned(player)
        val totalPriceForHotels = pricePerHotel * getNumberOfHotelsOwned(player)
        player.pay(totalPriceForHouses + totalPriceForHotels)
    }

    fun payIfAffordable(player: PlayerViewModel, price: Int): Boolean{
        if(player.money.value<price)
            return false
        pay(player, price)
        return true
    }

    fun addToGatheredTaxes(amount: Int){
        _gatheredTaxes.value += amount
    }

    fun collectGatheredTaxes(){
        if(gatheredTaxes.value>0){
            val player = getActivePlayer()!!
            player.receive(gatheredTaxes.value)
            resetGatheredTaxes()
        }
    }

    fun dismissPaymentPopup() {
        _showPaymentPopup.value = false
    }

    fun showPaymentPopup(
        payer: PlayerViewModel,
        receiver: PlayerViewModel,
        amount: Int,
        onDismissAction: () -> Unit
    ) {
        _paymentDetails.value = PaymentDetails(payer, receiver, amount)
        _showPaymentPopup.value = true
        serviceScope.launch {
            delay(1500)
            dismissPaymentPopup()
            onDismissAction()
        }
    }

    private fun resetGatheredTaxes() {
        _gatheredTaxes.value = 0
    }

    private fun calculateRentPrice(estate: EstateViewModel): Int{
        var ret = 0
        if(estate.isProperty){
            ret = if(RuleBook.doubleRentOnCollectedSetsEnabled
                && estate.numberOfBuildings.value==0
                && doesPlayerOwnASet(estate.ownerIndex.value,(estate.estate as Property).setColor)
            ){
                estate.estate.rent[0] * 2
            } else {
                estate.estate.rent[estate.numberOfBuildings.value]
            }
        }
        if(estate.isUtility){
            val utilitiesOwned = estates.value.filter { e -> e.isUtility && e.ownerIndex.value == estate.ownerIndex.value && !e.isMortgaged.value }.size
            val diceMultiplier = getDiceSum()
            ret = estate.estate.rent[utilitiesOwned - 1] * diceMultiplier
        }
        if(estate.isStation){
            val stationsOwned = estates.value.filter { e -> e.isStation && e.ownerIndex.value == estate.ownerIndex.value && !e.isMortgaged.value }.size
            ret = estate.estate.rent[stationsOwned - 1]
        }
        ret *= priceMultiplier
        priceMultiplier = 1
        return ret
    }
}

data class PaymentDetails(
    val payer: PlayerViewModel,
    val receiver: PlayerViewModel,
    val amount: Int
)