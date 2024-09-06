package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

@Composable
fun PlayerDrawer(
    canvasWidth: Dp,
    canvasHeight: Dp,
    playersVM: PlayerViewModel,
    fieldIndex: Int,
    circleSize: Dp = 6.dp,
    spacing: Dp = 4.dp
) {
    val players = playersVM.players.collectAsState().value
    val playerPositions = playersVM.playerPositions.collectAsState().value

    // Filter players who are on the specified field
    val playersOnField = players.filterIndexed { playerIndex, _ ->
        playerIndex < playerPositions.size && playerPositions[playerIndex] == fieldIndex
    }

    Canvas(modifier = Modifier.size(canvasWidth, canvasHeight)) {
        val widthPx = canvasWidth.toPx()
        val heightPx = canvasHeight.toPx()

        val rows = when (playersOnField.size) {
            6, 5 -> 3
            4, 3, 2 -> 2
            else -> 1
        }
        val cols = when (playersOnField.size) {
            6, 5, 4, 3 -> 2
            else -> 1
        }

        val totalWidthPx = (circleSize + spacing) * cols - spacing
        val totalHeightPx = (circleSize + spacing) * rows - spacing
        val startX = (widthPx - totalWidthPx.toPx()) / 2
        val startY = (heightPx - totalHeightPx.toPx()) / 2

        // Draw each player who is on the current field
        playersOnField.forEachIndexed { idx, player ->
            val row = idx / cols
            val col = idx % cols
            val x = startX + (col * (circleSize + spacing)).toPx()
            val y = startY + (row * (circleSize + spacing)).toPx()
            val radius = circleSize.toPx() / 2

            // Draw the border circle
            drawCircle(
                color = lerp(player.color, Color.Black, 0.2f),
                radius = radius + 1.dp.toPx(),
                center = Offset(x + radius, y + radius)
            )

            // Draw the player circle
            drawCircle(
                color = player.color,
                radius = radius,
                center = Offset(x + radius, y + radius)
            )
        }
    }
}