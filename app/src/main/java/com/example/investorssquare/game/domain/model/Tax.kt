package com.example.investorssquare.game.domain.model

class Tax(
    override val name: String,
    override val index:Int,
    val tax: Int,
    val taxPercentage: Int
) : Field(name, FieldType.TAX, index)