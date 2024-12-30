package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.EstateService.estates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BankruptcyService {
    private val _debtPopupVisible = MutableStateFlow(false)
    val debtPopupVisible: StateFlow<Boolean> get() = _debtPopupVisible

    fun dismissDebtPopup(){
        _debtPopupVisible.value = false
    }
    fun declareBankruptcy(player: PlayerViewModel){
        player.bankrupt()
        dismissDebtPopup()
    }
    fun openDebtPopup(){
        _debtPopupVisible.value= true
    }
    fun calculateNetValue(pVM: PlayerViewModel): Int{
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