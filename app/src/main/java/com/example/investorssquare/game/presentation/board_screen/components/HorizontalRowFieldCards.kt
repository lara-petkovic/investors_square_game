package com.example.investorssquare.game.presentation.board_screen.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun HorizontalRowFieldCards(
    fieldHeight: Dp,
    fieldWidth: Dp,
    modifier: Modifier = Modifier,
    startIndex: Int
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(fieldHeight)
            .padding(
                start = fieldHeight,
                end = fieldHeight
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..9) {
            Card(
                modifier = Modifier
                    .size(fieldWidth, fieldHeight)
                    .clickable {
                        val index = (if (startIndex < 15) 10 - i else i) + startIndex
                        Toast.makeText(context, "$index square clicked!", Toast.LENGTH_SHORT).show()
                    },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 1.dp, color = Color.Black)
            ) {
            }
        }
    }
}
