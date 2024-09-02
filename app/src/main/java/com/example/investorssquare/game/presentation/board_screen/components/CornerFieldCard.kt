package com.example.investorssquare.game.presentation.board_screen.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CornerFieldCard(
    fieldSize: Dp,
    modifier: Modifier = Modifier,
    index: Int
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .size(fieldSize)
            .clickable {
                Toast.makeText(context, "$index square clicked!", Toast.LENGTH_SHORT).show()
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {

    }
}
