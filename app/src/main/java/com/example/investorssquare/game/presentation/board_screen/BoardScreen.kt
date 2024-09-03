package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.util.Constants

@Composable
fun BoardScreen(players: List<Player>, board: Board) {
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
        Board(screenWidthDp, sideMargin, board)
        PlayerCardColumns(players = players, screenWidthDp = screenWidthDp.value.toInt())
    }
}