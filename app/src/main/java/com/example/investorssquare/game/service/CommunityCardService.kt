package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.CommunityCard
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.BoardService.showPopupForField
import com.example.investorssquare.game.service.PlayersService.getActivePlayer

object CommunityCardService {
    private var card: CommunityCard? = null
    fun drawCard(isChance:Boolean) : CommunityCard{
        if (isChance)
            card = board.value?.chance?.drawCard()!!
        else
            card = board.value?.communityChest?.drawCard()!!
        return card as CommunityCard
    }
    fun openCardPopup(){
        val player = getActivePlayer()!!
        showPopupForField(player.position.value)
    }
    fun executeCardAction(){
        when(card?.actionCode){
            1 -> CommunityCardActionsService.action1()
            2 -> CommunityCardActionsService.action2()
            3 -> CommunityCardActionsService.action3()
            4 -> CommunityCardActionsService.action4()
            5 -> CommunityCardActionsService.action5()
            6 -> CommunityCardActionsService.action6()
            7 -> CommunityCardActionsService.action7()
            8 -> CommunityCardActionsService.action8()
            9 -> CommunityCardActionsService.action9()
            10 -> CommunityCardActionsService.action10()
            11 -> CommunityCardActionsService.action11()
            12 -> CommunityCardActionsService.action12()
            13 -> CommunityCardActionsService.action13()
            14 -> CommunityCardActionsService.action14()
            15 -> CommunityCardActionsService.action15()
            16 -> CommunityCardActionsService.action16()
            17 -> CommunityCardActionsService.action17()
            18 -> CommunityCardActionsService.action18()
            19 -> CommunityCardActionsService.action19()
            20 -> CommunityCardActionsService.action20()
            21 -> CommunityCardActionsService.action21()
            22 -> CommunityCardActionsService.action22()
            23 -> CommunityCardActionsService.action23()
            24 -> CommunityCardActionsService.action24()
            25 -> CommunityCardActionsService.action25()
            26 -> CommunityCardActionsService.action26()
            27 -> CommunityCardActionsService.action27()
            28 -> CommunityCardActionsService.action28()
            29 -> CommunityCardActionsService.action29()
            30 -> CommunityCardActionsService.action30()
            31 -> CommunityCardActionsService.action31()
            32 -> CommunityCardActionsService.action32()
            else -> {}
        }
    }
}