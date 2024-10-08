package com.example.investorssquare.game.domain.model

open class Field (
    open val name: String,
    open val type: FieldType,
    open val index: Int
){
    override fun toString(): String {
        return "$index: $name"
    }
}