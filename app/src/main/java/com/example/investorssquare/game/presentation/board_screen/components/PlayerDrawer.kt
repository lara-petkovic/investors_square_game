package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.util.Constants.NUMBER_OF_FIELDS

@Composable
fun PlayerDrawer(
    canvasWidth: Dp,
    canvasHeight: Dp,
    boardVM: BoardViewModel,
    fieldIndex: Int,
    imageSize: Dp = 16.dp,
    spacing: Dp = (-5).dp
) {
    val players = boardVM.players.collectAsState().value
    val playerPositions = boardVM.playersPositions.collectAsState().value

    val indexesOfPlayersOnTheField = players.filterIndexed { playerIndex, _ ->
        playerIndex < playerPositions.size && playerPositions[playerIndex] == fieldIndex
    }

    Box(modifier = Modifier.size(canvasWidth, canvasHeight)) {
        val (rows, cols) = when (indexesOfPlayersOnTheField.size) {
            6 -> 3 to 2
            5 -> 3 to 2
            4 -> 2 to 2
            3 -> 2 to 2
            2 -> 1 to 2
            else -> 1 to 1
        }

        val totalWidthPx = (imageSize + spacing) * cols - spacing
        val totalHeightPx = (imageSize + spacing) * rows - spacing

        val startX = (canvasWidth - totalWidthPx) / 2
        val startY = (canvasHeight - totalHeightPx) / 2

        indexesOfPlayersOnTheField.forEachIndexed { idx, player ->
            val row = idx / cols
            val col = idx % cols
            val x = startX + (col * (imageSize + spacing))
            val y = startY + (row * (imageSize + spacing))

            val playerImageRes = imageByColor(player)
            val playerPosition by player.position.collectAsState()
            val prevPosition by rememberUpdatedState(playerPosition - 1)

            val animX = remember { Animatable(x.value) }
            val animY = remember { Animatable(y.value) }

            LaunchedEffect(playerPosition, indexesOfPlayersOnTheField.size) {
                val durationMillis = 500
                animX.animateTo(
                    targetValue = x.value,
                    animationSpec = tween(durationMillis = durationMillis)
                )
                animY.animateTo(
                    targetValue = y.value,
                    animationSpec = tween(durationMillis = durationMillis)
                )
            }

            Image(
                painter = painterResource(id = playerImageRes),
                contentDescription = "Player Icon",
                modifier = Modifier
                    .size(imageSize)
                    .offset(animX.value.dp, animY.value.dp)
                    .graphicsLayer(rotationZ = playerRotationAngle(playerPosition))
            )
        }
    }
}

private fun playerRotationAngle(playerPosition: Int): Float {
    val rotationAngle = when (playerPosition % NUMBER_OF_FIELDS) {
        in 11..19 -> -90f
        in 21..29 -> -180f
        in 31..39 -> 90f
        else -> 0f
    }
    return rotationAngle
}

private fun imageByColor(playerViewModel: PlayerViewModel): Int {
    val playerImageRes = when (playerViewModel.color.value) {
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