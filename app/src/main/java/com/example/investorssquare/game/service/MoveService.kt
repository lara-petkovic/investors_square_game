package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.Game

object MoveService {
    fun handleDiceToTheNextPlayer() {
        Game.diceViewModel.enableDiceButton()
        Game.hideFinishButton()
        val activePlayer = Game.getActivePlayer()!!
        activePlayer.finishMove()
        val nextPlayersIndex = (activePlayer.index.value+1) % Game.players.value.size
        Game.players.value[nextPlayersIndex].startMove()
    }
}