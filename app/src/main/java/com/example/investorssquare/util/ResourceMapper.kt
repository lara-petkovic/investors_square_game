package com.example.investorssquare.util

import com.example.investorssquare.R

object ResourceMapper {
    private val imageMap = mapOf(
        "default" to R.drawable.default_board,
        "default_house" to R.drawable.default_house,
        "default_hotel" to R.drawable.default_hotel,

        "nba" to R.drawable.default_board,
        "nba_house" to R.drawable.default_house,
        "nba_hotel" to R.drawable.default_hotel,
    )

    fun getImageResource(imageName: String): Int? {
        return imageMap[imageName]
    }
}