package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel

@Composable
fun PlayerCardColumns(boardVM: BoardViewModel) {
    val players by boardVM.players.collectAsState()

    val columnsCount = 2
    val rowsCount = (players.size + columnsCount - 1) / columnsCount

    val rows = (0 until rowsCount).map { rowIndex ->
        (0 until columnsCount).mapNotNull { columnIndex ->
            players.getOrNull(rowIndex * columnsCount + columnIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowPlayers ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                rowPlayers.forEach { player ->
                    PlayerCard(
                        playerViewModel = player
                    )
                }
            }
        }
    }
}