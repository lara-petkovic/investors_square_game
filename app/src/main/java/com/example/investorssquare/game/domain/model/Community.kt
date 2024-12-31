package com.example.investorssquare.game.domain.model

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class Community(
    val imageUrl: String,
    val commonName: String,
    val cards: MutableList<CommunityCard>,
    val primaryColor: Color
){
    fun shuffleCards() {
        cards.shuffle(Random.Default)
    }

    fun drawCard(): CommunityCard {
        cards.moveFirstToEnd()
        return cards[cards.size-1]
    }

    private fun <T> MutableList<T>.moveFirstToEnd() {
        if (this.isNotEmpty()) {
            val firstElement = this.removeAt(0)
            this.add(firstElement)
        }
    }
}