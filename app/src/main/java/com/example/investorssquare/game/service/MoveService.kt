package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.highlighting_services.BuildingService

object MoveService {
    fun handleDiceToTheNextPlayer() {
        val activePlayer = Game.getActivePlayer() ?: return
        activePlayer.finishMove()
        BuildingService.resetBuildingsInCurrentMove()

        var nextPlayersIndex = (activePlayer.index.value + 1) % Game.players.value.size
        var nextPlayer = Game.players.value[nextPlayersIndex]
        while(nextPlayer.isInBankruptcy.value){
            nextPlayersIndex = (nextPlayersIndex+1) % Game.players.value.size
            nextPlayer = Game.players.value[nextPlayersIndex]
        }
        nextPlayer.startMove()

        if(nextPlayer.isInJail.value){
            Game.showPopupForField(nextPlayer.position.value)
        }
        else{
            Game.diceViewModel.enableDiceButton()
            Game.hideFinishButton()
        }
    }
}