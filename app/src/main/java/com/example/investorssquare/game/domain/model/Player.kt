package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

data class Player(
    val name: String,
    val color: Color,
    var properties: List<Property> = emptyList()
)