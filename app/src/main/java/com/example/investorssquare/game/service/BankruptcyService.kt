package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.players
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object BankruptcyService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val _debtPopupVisible = MutableStateFlow(false)
    val debtPopupVisible: StateFlow<Boolean> get() = _debtPopupVisible

    fun dismissDebtPopup() {
        _debtPopupVisible.value = false
    }

    fun declareBankruptcy(player: PlayerViewModel) {
        player.bankrupt()
        dismissDebtPopup()
        val playersLeft = players.value.filter{p->!p.isInBankruptcy.value}
        if(playersLeft.size==1){
            serviceScope.launch { EventBus.postEvent(Event.ON_PLAYER_WON(playersLeft[0].index.value)) }
        }
    }

    fun openDebtPopup() {
        _debtPopupVisible.value= true
    }

    fun calculateNetValue(pVM: PlayerViewModel): Int {
        var netValue = 0
        for(property in estates.value.filter { e->e.isOwnedByPlayer(pVM) }){
            if(property.numberOfBuildings.value>0){
                netValue += property.numberOfBuildings.value * (property.estate as Property).housePrice/2
            }
            if(RuleBook.mortgagesEnabled){
                netValue += property.estate.mortgagePrice
            }
            if(RuleBook.sellingEstateEnabled){
                netValue += property.estate.sellPrice
            }
        }
        return netValue
    }
}