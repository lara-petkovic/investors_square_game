package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

object EstateService {
    fun showPopupForEstate(){
        Game.showPopupForField(Game.getActivePlayer()?.position?.value!!)
    }
    fun handleCardInformationClick(fieldIndex: Int) {
        val field = Game.getEstateByFieldIndex(fieldIndex)
        if (field != null) {
            Game.showPopupForField(fieldIndex)
        }
    }
    fun handleEstateBought(index: Int){
        val player = Game.getActivePlayer()!!
        val estate = Game.getEstateByFieldIndex(index)!!
        estate.setOwnerIndex(player.index.value)
        Game.dismissPopup()
    }
    fun getNumberOfHousesOwned(player: PlayerViewModel): Int{
        val ownedEstates = Game.estates.value.filter { e-> e.ownerIndex.value==player.index.value }
        var ret = 0
        for(estate in ownedEstates){
            if(estate.numberOfBuildings.value < estate.estate.rent.size - 1){
                ret += estate.numberOfBuildings.value
            }
        }
        return ret
    }
    fun getNumberOfHotelsOwned(player: PlayerViewModel): Int{
        val ownedEstates = Game.estates.value.filter { e-> e.ownerIndex.value==player.index.value }
        var ret = 0
        for(estate in ownedEstates){
            if(estate.numberOfBuildings.value == estate.estate.rent.size - 1){
                ret ++
            }
        }
        return ret
    }

}