package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.turnOffHighlightMode
import com.example.investorssquare.game.service.BoardService.turnOnHighlightMode
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService
import com.example.investorssquare.game.service.TransactionService.receive
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
        turnOnHighlightMode()
        _mortgageModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWhichPlayerCanMortgage(getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffMortgageMode(){
        if(_mortgageModeOn.value){
            _mortgageModeOn.value = false
            turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    fun mortgage(estate: EstateViewModel) {
        if (estate.isMortgaged.value || !RuleBook.mortgagesEnabled || !estate.isHighlighted.value)
            return
        estate.mortgage()
        val property = estate.estate
        val player = getActivePlayer()!!
        receive(player, property.mortgagePrice)
        highlightProperties()
    }
    private fun getPropertiesWhichPlayerCanMortgage(player: PlayerViewModel): List<EstateViewModel>{
        if(!RuleBook.mortgagesEnabled)
            return emptyList()
        val propertiesWhichPlayerCanMortgage = estates.value.filter { e->
            e.isOwnedByPlayer(player) && !e.isMortgaged.value && e.numberOfBuildings.value==0
        }
        return propertiesWhichPlayerCanMortgage
    }
}