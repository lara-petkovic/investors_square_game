package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game

object JailEscapeService {
    fun bailOut(){
        val price = RuleBook.jailEscapePrice
        val player = Game.getActivePlayer()!!
        if(TransactionService.payIfAffordable(player, price)){
            Game.addToGatheredTaxes(price)
            player.escapeJail()
            Game.diceViewModel.enableDiceButton()
            Game.hideFinishButton()
            Game.dismissPopup()
        }
    }
    fun rollADouble(){
        Game.dismissPopup()
        Game.diceViewModel.enableDiceButton()
    }
    fun useGetOutOfJailFreeCard(){
        val player = Game.getActivePlayer()!!
        if(player.numberOfGetOutOfJailFreeCards.value>0){
            player.useGetOutOfJailFreeCard()
            player.escapeJail()
            Game.diceViewModel.enableDiceButton()
            Game.hideFinishButton()
            Game.dismissPopup()
        }
    }
}