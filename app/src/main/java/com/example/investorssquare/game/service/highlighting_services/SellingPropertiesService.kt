package com.example.investorssquare.game.service.highlighting_services

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.TransactionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SellingPropertiesService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _sellingModeOn = MutableStateFlow<Boolean>(false)
    val sellingModeOn: StateFlow<Boolean> get() = _sellingModeOn

    fun switchSellingMode(){
        if(sellingModeOn.value)
            turnOffSellingMode()
        else
            turnOnSellingMode()
    }
    fun turnOnSellingMode(){
        _sellingModeOn.value = true
        Game.turnOnHighlightMode()
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getEstatesWhichPlayerCanSell(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffSellingMode(){
        if(_sellingModeOn.value){
            _sellingModeOn.value = false
            Game.turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    private fun getEstatesWhichPlayerCanSell(player: PlayerViewModel): List<EstateViewModel>{
        val estatesWhichPlayerCanSell = Game.estates.value.filter{ p->
            p.isOwnedByPlayer(player) && p.numberOfBuildings.value==0
        }
        return if(Game.ruleBook.sellingEstateEnabled){
            estatesWhichPlayerCanSell
        } else
            emptyList()
    }
    fun sell(estate: EstateViewModel){
        if(estate.numberOfBuildings.value>0)
            return
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, estate.estate.sellPrice)
        player.sellEstate(estate)
        highlightProperties()
    }
}