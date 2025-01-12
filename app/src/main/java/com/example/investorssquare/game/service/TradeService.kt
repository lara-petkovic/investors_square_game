package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.EstateService.getEstateByFieldIndex
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.PlayersService.players
import com.example.investorssquare.game.service.TransactionService.pay
import com.example.investorssquare.game.service.TransactionService.receive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TradeService {
    private val _creatingTradeOffer = MutableStateFlow(false)
    val creatingTradeOffer: StateFlow<Boolean> get() = _creatingTradeOffer

    private val _reviewingTradeOffer = MutableStateFlow(false)
    val reviewingTradeOffer: StateFlow<Boolean> get() = _reviewingTradeOffer

    private val _counterTradeOffer = MutableStateFlow(false)
    val counterTradeOffer: StateFlow<Boolean> get() = _counterTradeOffer

    private val _initiatorPlayer = MutableStateFlow<PlayerViewModel?>(null)
    val initiatorPlayer: StateFlow<PlayerViewModel?> get() = _initiatorPlayer

    private val _targetedPlayer = MutableStateFlow<PlayerViewModel?>(null)
    val targetedPlayer: StateFlow<PlayerViewModel?> get() = _targetedPlayer

    private val _moneyToSend = MutableStateFlow(0)
    val moneyToSend: StateFlow<Int> get() = _moneyToSend

    private val _moneyToReceive = MutableStateFlow(0)
    val moneyToReceive: StateFlow<Int> get() = _moneyToReceive

    private val _initiatorPlayersEstates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val initiatorPlayersEstates: StateFlow<List<EstateViewModel>> get() = _initiatorPlayersEstates

    private val _targetedPlayersEstates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val targetedPlayersEstates: StateFlow<List<EstateViewModel>> get() = _targetedPlayersEstates

    fun openTradeWindow(){
        _initiatorPlayer.value = getActivePlayer()!!
        _initiatorPlayersEstates.value = estates.value.filter{e->e.ownerIndex.value== _initiatorPlayer.value!!.index.value && e.numberOfBuildings.value==0}
        _creatingTradeOffer.value = true
        _targetedPlayer.value = players.value.filter{ p-> p.index.value == (_initiatorPlayer.value!!.index.value + 1) % players.value.size}[0]
        _targetedPlayersEstates.value = estates.value.filter{e->e.ownerIndex.value== _targetedPlayer.value!!.index.value && e.numberOfBuildings.value==0}
    }

    fun estateClicked(index: Int){
        if(creatingTradeOffer.value || counterTradeOffer.value){
            val e = getEstateByFieldIndex(index)!!
            if(e.isIncludedInTrade.value)
                e.excludeFromTrade()
            else
                e.includeInTrade()
        }
    }

    fun changeTargetedPlayer(next: Boolean){
        _targetedPlayersEstates.value.forEach { e-> e.excludeFromTrade() }
        var nextPlayerIndex = (_targetedPlayer.value!!.index.value + (if(next) 1 else -1)) % players.value.size
        if(nextPlayerIndex== _initiatorPlayer.value!!.index.value){
            nextPlayerIndex = (nextPlayerIndex+1) % players.value.size
        }
        _targetedPlayer.value = players.value.filter{ p-> p.index.value == nextPlayerIndex}[0]
        _targetedPlayersEstates.value = estates.value.filter{e->e.ownerIndex.value== _targetedPlayer.value!!.index.value && e.numberOfBuildings.value==0}
    }

    fun createTradeOffer(){
        val estatesToSend = _initiatorPlayersEstates.value.filter{e->e.isIncludedInTrade.value}
        val estatesToReceive = _targetedPlayersEstates.value.filter{e->e.isIncludedInTrade.value}
        if((moneyToSend.value==0 && estatesToSend.isEmpty())
            || (moneyToReceive.value==0 && estatesToReceive.isEmpty())
            || (estatesToSend.isEmpty() && estatesToReceive.isEmpty())){
            invalidTrade()
            return
        }
        _creatingTradeOffer.value = false
        _counterTradeOffer.value = false
        _reviewingTradeOffer.value = true
        rotateSides()
    }

    fun acceptTradeOffer(){
        val estatesToSend = _initiatorPlayersEstates.value.filter{e->e.isIncludedInTrade.value}
        val estatesToReceive = _targetedPlayersEstates.value.filter{e->e.isIncludedInTrade.value}
        estatesToSend.forEach { e->e.setOwnerIndex(_targetedPlayer.value!!.index.value) }
        estatesToReceive.forEach { e->e.setOwnerIndex(_initiatorPlayer.value!!.index.value) }
        receive(_initiatorPlayer.value!!, _moneyToReceive.value)
        receive(_targetedPlayer.value!!, _moneyToSend.value)
        pay(_initiatorPlayer.value!!, _moneyToSend.value)
        pay(_targetedPlayer.value!!, _moneyToReceive.value)
        reset()
    }

    fun declineTradeOffer(){
        reset()
    }

    fun counterOffer(){
        _counterTradeOffer.value = true
    }

    fun cancelCounterOffer(){
        _counterTradeOffer.value = true
    }

    private fun rotateSides(){
        val i = _initiatorPlayer.value!!.index.value
        val t = _targetedPlayer.value!!.index.value
        _initiatorPlayer.value = players.value.filter{p->p.index.value == t}[0]
        _targetedPlayer.value = players.value.filter{p->p.index.value == i}[0]
        _initiatorPlayersEstates.value = estates.value.filter{e->e.ownerIndex.value== _initiatorPlayer.value!!.index.value && e.numberOfBuildings.value==0}
        _targetedPlayersEstates.value = estates.value.filter{e->e.ownerIndex.value== _targetedPlayer.value!!.index.value && e.numberOfBuildings.value==0}
        val sendingMoney = moneyToSend.value
        _moneyToSend.value = moneyToReceive.value
        _moneyToReceive.value = sendingMoney
    }

    private fun invalidTrade(){}

    private fun reset(){
        estates.value.forEach { e-> e.excludeFromTrade() }
        _reviewingTradeOffer.value = false
        _creatingTradeOffer.value = false
        _counterTradeOffer.value = false
    }
}