package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

class Board(
    val name: String,
    val imageUrl: String,
    val fields: List<Field>,
    val playerColors: List<Color>
)