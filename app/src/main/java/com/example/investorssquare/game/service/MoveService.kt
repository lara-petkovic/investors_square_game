package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.Game

object MoveService {
    fun handleDiceToTheNextPlayer() {
        val activePlayer = Game.getActivePlayer() ?: return
        activePlayer.finishMove()

        val nextPlayersIndex = (activePlayer.index.value + 1) % Game.players.value.size
        val nextPlayer = Game.players.value[nextPlayersIndex]
        nextPlayer.startMove()

        if(!nextPlayer.isInJail.value || Game.ruleBook.rollADoubleToEscapeJailEnabled){
            Game.diceViewModel.enableDiceButton()
            Game.hideFinishButton()
        }
    }
}