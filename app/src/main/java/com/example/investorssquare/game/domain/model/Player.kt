package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

data class Player(
    val name: String,
    val money: Int,
    val color: Color,
    val properties: List<Property> = emptyList()
)