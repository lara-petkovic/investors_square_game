package com.example.investorssquare.game.domain.model

class CommunityCard (
    var text: String,
    val actionCode: Int,
    val isChanceCard: Boolean
){
    override fun toString(): String {
        return "($actionCode) $text"
    }
}