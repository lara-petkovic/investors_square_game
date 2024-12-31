package com.example.investorssquare.game.service

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.dismissPopupForField
import com.example.investorssquare.game.service.BoardService.showPopupForField
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.PlayersService.players
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object EstateService {
    private val _estates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val estates: StateFlow<List<EstateViewModel>> get() = _estates

    fun setEstates(est: List<EstateViewModel>) {
        _estates.value = est
    }

    fun getAllPropertiesBySet(set: Color): List<EstateViewModel> {
        return estates.value.filter{e -> e.isProperty && (e.estate as Property).setColor == set }
    }

    fun doesPlayerOwnASet(ownerIndex: Int, set: Color): Boolean {
        return getAllPropertiesBySet(set).all{e -> e.ownerIndex.value == ownerIndex}
    }

    fun getOwnerOfEstate(fieldIndex: Int): PlayerViewModel? {
        val ownerIndex = getEstateByFieldIndex(fieldIndex)?.ownerIndex?.value
        return players.value.getOrNull(ownerIndex ?: -1).takeIf { ownerIndex != -1 }
    }

    fun getEstateByFieldIndex(index: Int): EstateViewModel? {
        return _estates.value.firstOrNull { it.estate.index == index }
    }

    fun showPopupForEstate(){
        showPopupForField(getActivePlayer()?.position?.value!!)
    }

    fun handleCardInformationClick(fieldIndex: Int) {
        val field = getEstateByFieldIndex(fieldIndex)
        if (field != null) {
            showPopupForField(fieldIndex)
        }
    }

    fun handleEstateBought(index: Int) {
        val player = getActivePlayer()!!
        val estate = getEstateByFieldIndex(index)!!
        estate.setOwnerIndex(player.index.value)
        dismissPopupForField()
    }

    fun getNumberOfHousesOwned(player: PlayerViewModel): Int {
        val ownedEstates = estates.value.filter { e-> e.ownerIndex.value==player.index.value }
        var ret = 0
        for(estate in ownedEstates){
            if(estate.numberOfBuildings.value < estate.estate.rent.size - 1){
                ret += estate.numberOfBuildings.value
            }
        }
        return ret
    }

    fun getNumberOfHotelsOwned(player: PlayerViewModel): Int {
        val ownedEstates = estates.value.filter { e-> e.ownerIndex.value==player.index.value }
        var ret = 0
        for(estate in ownedEstates){
            if(estate.numberOfBuildings.value == estate.estate.rent.size - 1){
                ret ++
            }
        }
        return ret
    }
}