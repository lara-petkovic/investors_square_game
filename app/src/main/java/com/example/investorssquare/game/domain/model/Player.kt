package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

data class Player(
    val name: String,
    var money: Int,
    val color: Color,
    var position: Int = 0,
    var properties: List<Property> = emptyList()
)