package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.util.Constants

@Composable
fun PlayerCardColumns(players: List<Player>, screenWidthDp: Int, activePlayerIndex: Int) {
    val columnsCount = 2
    val playersPerColumn = (players.size + columnsCount - 1) / columnsCount
    val columns = (0 until columnsCount).map { columnIndex ->
        players.drop(columnIndex * playersPerColumn).take(playersPerColumn)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        columns.forEachIndexed { index, columnPlayers ->
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = if (index == 0) Alignment.Start else Alignment.End
            ) {
                columnPlayers.forEachIndexed { i, player ->
                    PlayerCard(
                        player = player,
                        width = (screenWidthDp * Constants.PLAYER_CARD_WIDTH).dp,
                        isActive = (index * playersPerColumn + i) == activePlayerIndex
                    )
                }
            }
        }
    }
}