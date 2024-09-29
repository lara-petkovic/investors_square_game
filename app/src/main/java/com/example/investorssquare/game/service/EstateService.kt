package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        player.buyNewEstate(estate)
        Game.dismissPopup()
    }
}