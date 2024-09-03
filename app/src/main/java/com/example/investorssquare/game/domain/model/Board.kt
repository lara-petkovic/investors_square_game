package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

class Board(
    val name: String,
    val imageUrl: String,
    val houseImageUrl: String,
    val hotelImageUrl: String,
    val propertyCommonName: String,
    val propertyCommonNamePlural: String,
    val stationCommonName: String,
    val stationCommonNamePlural: String,
    val utilityCommonName: String,
    val utilityCommonNamePlural: String,
    val diceColor: Color,
    val fields: List<Field>,
    val playerColors: List<Color>
)