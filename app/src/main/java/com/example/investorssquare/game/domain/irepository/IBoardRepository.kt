package com.example.investorssquare.game.domain.irepository

interface IBoardRepository {
    suspend fun getFields() // : List<Something>
}