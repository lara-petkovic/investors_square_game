package com.example.investorssquare.game.domain.model

data class Property(
    val name: String,
    val houseRent: List<Int>, // list of 5 elements representing 4 houses and a hotel price
    val sold: Boolean = false,
    val mortgaged: Boolean = false
)