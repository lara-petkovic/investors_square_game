package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.Game

object MoveService {
    fun handleDiceToTheNextPlayer() {
        Game.diceViewModel.enableDiceButton()
        Game.hideFinishButton()

        val activePlayer = Game.getActivePlayer() ?: return
        activePlayer.finishMove()

        val players = Game.players.value
        if (players.isEmpty()) return

        val nextPlayersIndex = (activePlayer.index.value + 1) % players.size
        players[nextPlayersIndex].startMove()
    }
    fun handleMoveTimerElapsed(){
        if(Game.isFinishButtonVisible.value)
            handleDiceToTheNextPlayer()
        else
            DiceService.rollDice()
    }
}