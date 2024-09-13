package com.example.investorssquare.game.presentation.board_screen.viewModels

sealed interface BoardVMEvent  {
    data class OnFieldClicked(val fieldIndex: Int): BoardVMEvent
}