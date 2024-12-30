package com.example.investorssquare.game.service

import com.example.investorssquare.game.service.BoardService.showPopupForField
import com.example.investorssquare.game.service.DiceService.enableDice
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.PlayersService.players
import com.example.investorssquare.game.service.highlighting_services.BuildingService
import com.example.investorssquare.game.service.highlighting_services.BuildingService.resetBuildingsInCurrentMove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MoveService {
    private val _isFinishButtonVisible = MutableStateFlow(false)
    val isFinishButtonVisible: StateFlow<Boolean> get() = _isFinishButtonVisible

    fun showFinishButton() {
        _isFinishButtonVisible.value = true
    }
    fun hideFinishButton() {
        _isFinishButtonVisible.value = false
    }
    fun handleDiceToTheNextPlayer() {
        val activePlayer = getActivePlayer() ?: return
        activePlayer.finishMove()
        resetBuildingsInCurrentMove()

        var nextPlayersIndex = (activePlayer.index.value + 1) % players.value.size
        var nextPlayer = players.value[nextPlayersIndex]
        while(nextPlayer.isInBankruptcy.value){
            nextPlayersIndex = (nextPlayersIndex+1) % players.value.size
            nextPlayer = players.value[nextPlayersIndex]
        }
        nextPlayer.startMove()

        if(nextPlayer.isInJail.value){
            showPopupForField(nextPlayer.position.value)
        }
        else{
            enableDice()
        }
    }
}