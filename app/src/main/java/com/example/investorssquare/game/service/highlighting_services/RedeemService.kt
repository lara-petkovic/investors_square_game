package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.TransactionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RedeemService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _redeemModeOn = MutableStateFlow<Boolean>(false)
    val redeemModeOn: StateFlow<Boolean> get() = _redeemModeOn

    fun switchRedeemMode(){
        if(redeemModeOn.value)
            turnOffRedeemMode()
        else
            turnOnRedeemMode()
    }
    fun turnOnRedeemMode(){
        Game.turnOnHighlightMode()
        _redeemModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWhichPlayerCanRedeem(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffRedeemMode(){
        if(_redeemModeOn.value){
            _redeemModeOn.value = false
            Game.turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    fun redeem(estate: EstateViewModel){
        if(!estate.isMortgaged.value || !Game.ruleBook.mortgagesEnabled || !estate.isHighlighted.value)
            return

        val property = estate.estate
        val player = Game.getActivePlayer()!!
        if(TransactionService.payIfAffordable(player, property.mortgagePrice)){
            estate.Redeem()
            highlightProperties()
        }
    }
    private fun getPropertiesWhichPlayerCanRedeem(player: PlayerViewModel): List<EstateViewModel>{
        if(!Game.ruleBook.mortgagesEnabled)
            return emptyList()
        val propertiesWhichPlayerCanRedeem = Game.estates.value.filter { e->
            e.isOwnedByPlayer(player) && e.isMortgaged.value
        }
        return propertiesWhichPlayerCanRedeem
    }
}