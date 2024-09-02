package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCard
import com.example.investorssquare.util.Constants

@Composable
fun BoardScreen(players: List<Player>) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * Constants.SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * Constants.TOP_BOARD_MARGIN).dp

    val columnsCount = 2
    val playersPerColumn = players.size / columnsCount + if (players.size % columnsCount == 0) 0 else 1
    val columns = (0 until columnsCount).map { columnIndex ->
        players.drop(columnIndex * playersPerColumn).take(playersPerColumn)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sideMargin, end = sideMargin, top = topMargin),
        verticalArrangement = Arrangement.Top
    ) {
        Board(screenWidthDp, sideMargin)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topMargin),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            columns.forEachIndexed { index, columnPlayers ->
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = if (index == 0) Alignment.Start else Alignment.End
                ) {
                    columnPlayers.forEach { player ->
                        PlayerCard(player = player, width = (screenWidthDp.value * Constants.PLAYER_CARD_WIDTH).dp)
                    }
                }
            }
        }
    }
}