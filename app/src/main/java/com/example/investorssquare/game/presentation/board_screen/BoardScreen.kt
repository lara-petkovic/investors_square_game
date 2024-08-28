package com.example.investorssquare.game.presentation.board_screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.investorssquare.R

@Composable
fun BoardScreen() {
    val context = LocalContext.current
    Image(
        painter = painterResource(R.drawable.board),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 68.dp)
    ) {
        repeat(9) {
            Card(
                modifier = Modifier
                    .width(24.9.dp)
                    .height(56.5.dp)
                    .clickable {
                        Toast.makeText(context, "Card $it clicked!", Toast.LENGTH_SHORT).show()
                    },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Cyan),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Black
                )
            ) {

            }
        }
    }
}