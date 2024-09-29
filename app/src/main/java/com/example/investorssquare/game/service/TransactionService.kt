package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Tax
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object TransactionService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE -> payRent()
                    is Event.ON_BUYING_ESTATE -> buyEstate(event.fieldIndex)
                    is Event.ON_PLAYER_CROSSED_START -> collectSalary()
                    is Event.ON_PLAYER_LANDED_ON_TAX -> payTax()
                    else -> { }
                }
            }
        }
    }
    private fun collectSalary(){
        val player = Game.getActivePlayer()!!
        player.receive(Game.board.value?.ruleBook?.salary!!)
    }
    private fun buyEstate(index: Int){
        val player = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(index)!!
        if(player.money.value>=estate.estate.value.price){
            player.pay(estate.estate.value.price)
            serviceScope.launch {EventBus.postEvent(Event.ON_ESTATE_BOUGHT(index))}
        }
    }
    private fun payRent() {
        val payer = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(payer.position.value)!!
        val receiver = Game.getOwnerOfEstate(estate.estate.value.index)!!
        if(payer!=receiver){
            val moneyToTransfer = estate.estate.value.rent[0]
            payer.pay(moneyToTransfer)
            receiver.receive(moneyToTransfer)
            Game.showPaymentPopup(payer, receiver, moneyToTransfer)
        }
    }
    private fun payTax(){
        val player = Game.getActivePlayer()!!
        val field : Tax = Game.board.value?.fields?.get(player.position.value)!! as Tax
        player.pay(field.tax)
    }
}