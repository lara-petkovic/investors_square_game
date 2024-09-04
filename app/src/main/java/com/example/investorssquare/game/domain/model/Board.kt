package com.example.investorssquare.game.domain.model
import kotlin.random.Random
import androidx.compose.ui.graphics.Color

class Board(
    val name: String,
    val imageUrl: String,
    val houseImageUrl: String,
    val hotelImageUrl: String,
    val propertyCommonName: String,
    val propertyCommonNamePlural: String,
    val stationCommonName: String,
    val stationCommonNamePlural: String,
    val utilityCommonName: String,
    val utilityCommonNamePlural: String,
    val diceColor: Color,
    val communityChestPrimaryColor: Color,
    val chancePrimaryColor: Color,
    val communityChestCommonName: String,
    val chanceCommonName: String,
    val fields: List<Field>,
    val playerColors: List<Color>,
    val communityChestCards: MutableList<CommunityCard>,
    val chanceCards: MutableList<CommunityCard>
){
    fun shuffleCommunityCards(){
        communityChestCards.shuffle(Random.Default)
        chanceCards.shuffle(Random.Default)
    }
    fun drawChanceCard(): CommunityCard{
        chanceCards.moveFirstToEnd()
        return chanceCards[chanceCards.size-1]
    }
    fun drawCommunityChestCard(): CommunityCard{
        communityChestCards.moveFirstToEnd()
        return communityChestCards[communityChestCards.size-1]
    }
    private fun <T> MutableList<T>.moveFirstToEnd() {
        if (this.isNotEmpty()) {
            val firstElement = this.removeAt(0)
            this.add(firstElement)
        }
    }
}