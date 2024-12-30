package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.turnOffHighlightMode
import com.example.investorssquare.game.service.BoardService.turnOnHighlightMode
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService
import com.example.investorssquare.game.service.TransactionService.payIfAffordable
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

    private fun turnOnRedeemMode(){
        turnOnHighlightMode()
        _redeemModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWhichPlayerCanRedeem(getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffRedeemMode(){
        if(_redeemModeOn.value){
            _redeemModeOn.value = false
            turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    fun redeem(estate: EstateViewModel){
        if(!estate.isMortgaged.value || !RuleBook.mortgagesEnabled || !estate.isHighlighted.value)
            return

        val property = estate.estate
        val player = getActivePlayer()!!
        if(payIfAffordable(player, property.mortgagePrice)){
            estate.redeem()
            highlightProperties()
        }
    }
    private fun getPropertiesWhichPlayerCanRedeem(player: PlayerViewModel): List<EstateViewModel>{
        if(!RuleBook.mortgagesEnabled)
            return emptyList()
        val propertiesWhichPlayerCanRedeem = estates.value.filter { e->
            e.isOwnedByPlayer(player) && e.isMortgaged.value
        }
        return propertiesWhichPlayerCanRedeem.filter{p-> p.estate.mortgagePrice<=player.money.value}
    }
}