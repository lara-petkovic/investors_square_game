package com.example.investorssquare.game.domain.model

abstract class Estate(
    override val name: String,
    override val type: FieldType,
    override val index: Int,
    open val rent: List<Int>,
    open val price: Int,
    open val mortgagePrice: Int,
    open val sellPrice: Int
) : Field(name, type, index)