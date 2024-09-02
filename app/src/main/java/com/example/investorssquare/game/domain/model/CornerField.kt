package com.example.investorssquare.game.domain.model

class CornerField(
    override val name: String,
    override val index: Int,
    override val type: FieldType
) : Field(name, type, index)
