package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.CommunityCard
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object CommunityCardService {
    private var card: CommunityCard? = null
    fun drawCard(isChance:Boolean) : CommunityCard{
        if (isChance)
            card = Game.board.value?.chance?.drawCard()!!
        else
            card = Game.board.value?.communityChest?.drawCard()!!
        return card as CommunityCard
    }
    fun openCardPopup(){
        val player = Game.getActivePlayer()!!
        Game.showPopupForField(player.position.value)
    }
    fun executeCardAction(){
        Game.dismissPopup()
    }
}