package com.example.investorssquare.game.domain.model
import androidx.compose.ui.graphics.Color

class Board(
    val name: String,
    val imageUrl: String,
    val houseImageUrl: String,
    val hotelImageUrl: String,
    val diceColor: Color,
    val fields: List<Field>,
    val playerColors: List<Color>,
    val chance: Community,
    val communityChest: Community,
){
    fun shuffleCommunityCards(){
        chance.shuffleCards()
        communityChest.shuffleCards()
    }
}