package com.example.investorssquare.game.presentation.board_screen.viewModels

sealed interface BoardVMEvent  {
    data object RollDice: BoardVMEvent
    data class OnFieldClicked(val fieldIndex: Int): BoardVMEvent
    data class BuyEstate(val fieldIndex: Int): BoardVMEvent
}