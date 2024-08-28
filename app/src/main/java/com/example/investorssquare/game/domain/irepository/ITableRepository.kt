package com.example.investorssquare.game.domain.irepository

interface ITableRepository {
    suspend fun getFields() // : List<Something>
}