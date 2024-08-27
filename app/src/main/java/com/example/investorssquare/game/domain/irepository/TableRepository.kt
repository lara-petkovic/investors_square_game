package com.example.investorssquare.game.domain.irepository

interface TableRepository {
    suspend fun getFields() // : List<Something>
}