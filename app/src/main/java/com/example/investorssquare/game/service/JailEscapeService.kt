package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.BoardService.dismissPopupForField
import com.example.investorssquare.game.service.DiceService.enableDice
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService.addToGatheredTaxes

object JailEscapeService {
    fun bailOut(){
        val price = RuleBook.jailEscapePrice
        val player = getActivePlayer()!!
        if(TransactionService.payIfAffordable(player, price)){
            addToGatheredTaxes(price)
            player.escapeJail()
            enableDice()
            dismissPopupForField()
        }
    }
    fun rollADoubleToEscape(){
        dismissPopupForField()
        enableDice()
    }
    fun useGetOutOfJailFreeCard(){
        val player = getActivePlayer()!!
        if(player.numberOfGetOutOfJailFreeCards.value>0){
            player.useGetOutOfJailFreeCard()
            player.escapeJail()
            enableDice()
            dismissPopupForField()
        }
    }
}