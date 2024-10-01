package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlin.math.roundToInt

@Composable
fun HousesLayout(
    numberOfHouses: Int,
    houseSize: Dp = 30.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val spacing = 8.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        for (i in 0 until numberOfHouses) {
            val position = calculateHousePosition(i, numberOfHouses, houseSize, spacing)
            HouseImage(
                modifier = Modifier.offset {
                    IntOffset(
                        position.x.roundToInt(),
                        position.y.roundToInt()
                    )
                }
            )
        }
    }
}

fun calculateHousePosition(index: Int, numberOfHouses: Int, houseSize: Dp, spacing: Dp): Offset {
    val rowSize = houseSize.value + spacing.value

    return when (numberOfHouses) {
        1 -> Offset(0f, 0f)
        2 -> Offset(
            x = if (index == 0) -rowSize / 2 else rowSize / 2,
            y = 0f
        )
        3 -> {
            val x = if (index < 2) (index - 0.5f) * rowSize else 0f
            val y = if (index < 2) 0f else rowSize
            Offset(x, y)
        }
        4 -> {
            val x = if (index % 2 == 0) -rowSize / 2 else rowSize / 2
            val y = if (index < 2) 0f else rowSize
            Offset(x, y)
        }
        else -> Offset(0f, 0f)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun HouseImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Image(
        painter = painterResource(context.resources.getIdentifier(Game.board.value?.houseImageUrl, "drawable", context.packageName)),
        contentDescription = null,
        modifier = modifier
            .size(10.dp)
            .padding(0.5.dp)
    )
}