package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.CornerFieldCard
import com.example.investorssquare.game.presentation.board_screen.components.HorizontalRowFieldCards
import com.example.investorssquare.game.presentation.board_screen.components.VerticalRowFieldCards
import com.example.investorssquare.util.Constants

@Composable
@Preview
fun BoardScreen() {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * Constants.SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * Constants.TOP_BOARD_MARGIN).dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sideMargin, end = sideMargin, top = topMargin),
        verticalArrangement = Arrangement.Top
    ) {
        Board(screenWidthDp, sideMargin)
    }
}