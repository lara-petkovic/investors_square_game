package com.example.investorssquare.game.domain.model

abstract class Field (
    open val name: String,
    open val type: FieldType,
    open val index: Int
)