package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color

class Property(
    override val name: String,
    override val index: Int,
    override val rent: List<Int>,
    override val price: Int,
    override val mortgagePrice: Int,
    override val sellPrice: Int,
    override val imageUrl: String,
    val setColor: Color,
    val housePrice: Int,
    override val commonName: String,
    override val commonNamePlural: String
) : Estate(name, FieldType.PROPERTY, index, rent, price, mortgagePrice, sellPrice, imageUrl, commonName, commonNamePlural)