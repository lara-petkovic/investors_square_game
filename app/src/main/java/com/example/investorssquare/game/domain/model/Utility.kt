package com.example.investorssquare.game.domain.model

class Utility(
    override val name: String,
    override val index: Int,
    override val rent: List<Int>,
    override val price: Int,
    override val mortgagePrice: Int,
    override val sellPrice: Int
) : Estate(name, FieldType.UTILITY, index, rent, price, mortgagePrice, sellPrice)
