package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PaymentPopupCard(
    payer: PlayerViewModel,
    receiver: PlayerViewModel,
    amount: Int,
    onDismissRequest: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f))
            .clickable { onDismissRequest() }
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Payer section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = imageByColor(payer)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = payer.name.value)
                }

                // Arrow with amount
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "$amount", color = Color.Black)
                    Image(
                        modifier = Modifier.clip(RectangleShape),
                        painter = rememberDrawablePainter(
                            drawable = getDrawable(
                                LocalContext.current,
                                R.drawable.gif_arrow
                            )
                        ),
                        contentDescription = "Loading animation",
                        contentScale = ContentScale.FillWidth,
                    )
                }

                // Receiver section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = imageByColor(receiver)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(30.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = receiver.name.value)
                }
            }
        }
    }
}

fun imageByColor(playerViewModel: PlayerViewModel): Int {
    return when (playerViewModel.color.value) {
        Color.Magenta -> R.drawable.player_purple
        Color.Cyan -> R.drawable.player_cyan
        Color.Green -> R.drawable.player_green
        Color.Blue -> R.drawable.player_blue
        Color.Gray -> R.drawable.player_gray
        Color.DarkGray -> R.drawable.player_brown
        Color.White -> R.drawable.player_orange
        Color.Red -> R.drawable.player_red
        Color.Yellow -> R.drawable.player_yellow
        Color.Black -> R.drawable.player_black
        else -> R.drawable.player_black
    }
}