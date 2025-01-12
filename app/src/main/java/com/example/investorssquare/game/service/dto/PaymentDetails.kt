package com.example.investorssquare.game.service.dto

import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

data class PaymentDetails(
    val payer: PlayerViewModel,
    val receiver: PlayerViewModel,
    val amount: Int
)