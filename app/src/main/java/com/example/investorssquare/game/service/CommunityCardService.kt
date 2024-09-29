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
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private var card: CommunityCard? = null
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.ON_COMMUNITY_CARD_CLOSED -> executeCardAction()
                    is Event.ON_COMMUNITY_CARD_OPENED -> openCardPopup()
                    else -> { }
                }
            }
        }
    }
    fun drawCard(isChance:Boolean) : CommunityCard{
        if (isChance)
            card = Game.board.value?.chance?.drawCard()!!
        else
            card = Game.board.value?.communityChest?.drawCard()!!
        return card as CommunityCard
    }
    private fun openCardPopup(){
        val player = Game.getActivePlayer()!!
        Game.showPopupForField(player.position.value)
    }
    private fun executeCardAction(){
        Game.dismissPopup()
    }
}