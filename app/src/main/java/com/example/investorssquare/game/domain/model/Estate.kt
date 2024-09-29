package com.example.investorssquare.game.domain.model

open class Estate(
    override val name: String,
    override val type: FieldType,
    override val index: Int,  //index of the field on the board (0 - 39)
    open val rent: List<Int>,
    open val price: Int,
    open val mortgagePrice: Int,
    open val sellPrice: Int,
    open val imageUrl: String,
    open val commonName: String,
    open val commonNamePlural: String
) : Field(name, type, index)