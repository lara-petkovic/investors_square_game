package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.BoardService.turnOffHighlightMode
import com.example.investorssquare.game.service.BoardService.turnOnHighlightMode
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService.receive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SellingPropertiesService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _sellingModeOn = MutableStateFlow(false)
    val sellingPropertiesModeOn: StateFlow<Boolean> get() = _sellingModeOn

    fun switchSellingPropertiesMode() {
        if(sellingPropertiesModeOn.value)
            turnOffSellingPropertiesMode()
        else
            turnOnSellingPropertiesMode()
    }

    fun turnOffSellingPropertiesMode() {
        if(_sellingModeOn.value){
            _sellingModeOn.value = false
            turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }

    fun sellProperty(estate: EstateViewModel) {
        if(estate.numberOfBuildings.value>0)
            return
        val player = getActivePlayer()!!
        receive(player, estate.estate.sellPrice)
        estate.setOwnerIndex(-1)
        highlightProperties()
    }

    private fun turnOnSellingPropertiesMode() {
        _sellingModeOn.value = true
        turnOnHighlightMode()
        highlightProperties()
    }

    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getEstatesWhichPlayerCanSell(getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }

    private fun getEstatesWhichPlayerCanSell(player: PlayerViewModel): List<EstateViewModel> {
        val estatesWhichPlayerCanSell = estates.value.filter{ p->
            p.isOwnedByPlayer(player) && p.numberOfBuildings.value==0
        }
        return if(RuleBook.sellingEstateEnabled){
            estatesWhichPlayerCanSell
        } else
            emptyList()
    }
}