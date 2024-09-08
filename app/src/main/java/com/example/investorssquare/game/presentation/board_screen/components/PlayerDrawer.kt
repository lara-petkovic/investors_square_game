package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.investorssquare.R
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

@Composable
fun PlayerDrawer(
    canvasWidth: Dp,
    canvasHeight: Dp,
    playersVM: PlayerViewModel,
    fieldIndex: Int,
    imageSize: Dp = 16.dp,
    spacing: Dp = (-5).dp
) {
    val players = playersVM.players.collectAsState().value
    val playerPositions = playersVM.playerPositions.collectAsState().value

    val playersOnField = players.filterIndexed { playerIndex, _ ->
        playerIndex < playerPositions.size && playerPositions[playerIndex] == fieldIndex
    }

    Box(modifier = Modifier.size(canvasWidth, canvasHeight)) {
        val rows = when (playersOnField.size) {
            6, 5 -> 3
            4, 3, 2 -> 2
            else -> 1
        }
        val cols = when (playersOnField.size) {
            6, 5, 4, 3 -> 2
            else -> 1
        }

        val totalWidthPx = (imageSize + spacing) * cols - spacing
        val totalHeightPx = (imageSize + spacing) * rows - spacing

        val startX = (canvasWidth - totalWidthPx) / 2
        val startY = (canvasHeight - totalHeightPx) / 2

        playersOnField.forEachIndexed { idx, player ->
            val row = idx / cols
            val col = idx % cols
            val x = startX + (col * (imageSize + spacing))
            val y = startY + (row * (imageSize + spacing))

            val playerImageRes = imageByColor(player)

            Image(
                painter = painterResource(id = playerImageRes),
                contentDescription = "Player Icon",
                modifier = Modifier
                    .size(imageSize)
                    .offset(x, y)
            )
        }
    }
}

@Composable
private fun imageByColor(player: Player): Int {
    val playerImageRes = when (player.color) {
        Color.Magenta -> R.drawable.player_purple    //*
        Color.Cyan -> R.drawable.player_cyan
        Color.Green -> R.drawable.player_green
        Color.Blue -> R.drawable.player_blue
        Color.Gray -> R.drawable.player_gray
        Color.DarkGray -> R.drawable.player_brown    //*
        Color.White -> R.drawable.player_orange      //*
        Color.Red -> R.drawable.player_red
        Color.Yellow -> R.drawable.player_yellow
        Color.Black -> R.drawable.player_black
        else -> R.drawable.player_black
    }
    return playerImageRes
}