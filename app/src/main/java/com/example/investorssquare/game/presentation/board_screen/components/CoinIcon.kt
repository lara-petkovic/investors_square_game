package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.investorssquare.R

@Composable
fun CoinIcon(size: Dp) {
    Image(
        painter = painterResource(R.drawable.coin),
        contentDescription = null,
        modifier = Modifier.size(size)
    )
}