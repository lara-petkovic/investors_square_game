package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MortgageService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _mortgageModeOn = MutableStateFlow<Boolean>(false)
    val mortgageModeOn: StateFlow<Boolean> get() = _mortgageModeOn

    fun switchMortgageMode(){
        if(mortgageModeOn.value)
            turnOffMortgageMode()
        else
            turnOnMortgageMode()
    }
    fun turnOnMortgageMode(){
        Game.turnOnHighlightMode()
        _mortgageModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWhichPlayerCanMortgage(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffMortgageMode(){
        if(_mortgageModeOn.value){
            _mortgageModeOn.value = false
            Game.turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    fun mortgage(estate: EstateViewModel) {
        if (estate.isMortgaged.value || !Game.ruleBook.mortgagesEnabled || !estate.isHighlighted.value)
            return
        estate.Mortgage()
        val property = estate.estate
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, property.mortgagePrice)
        highlightProperties()
    }
    private fun getPropertiesWhichPlayerCanMortgage(player: PlayerViewModel): List<EstateViewModel>{
        if(!Game.ruleBook.mortgagesEnabled)
            return emptyList()
        val propertiesWhichPlayerCanMortgage = Game.estates.value.filter { e->
            e.isOwnedByPlayer(player) && !e.isMortgaged.value
        }
        return propertiesWhichPlayerCanMortgage
    }
}