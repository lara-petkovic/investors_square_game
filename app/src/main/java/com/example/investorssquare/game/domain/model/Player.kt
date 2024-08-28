package com.example.investorssquare.game.domain.model

data class Player(
    val name: String,
    val money: Int,
    val properties: List<Property>
)