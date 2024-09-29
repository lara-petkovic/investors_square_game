package com.example.investorssquare.game.service

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
                    is Event.PlayerLandedOnBoughtEstate -> payRent()
                    is Event.BuyingEstate -> buyEstate(event.fieldIndex)
                    is Event.PlayerCrossedStart -> collectSalary()
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
            serviceScope.launch {EventBus.postEvent(Event.EstateBought(index))}
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

}