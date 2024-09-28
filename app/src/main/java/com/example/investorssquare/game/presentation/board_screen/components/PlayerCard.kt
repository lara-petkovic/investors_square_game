package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.util.Constants.PLAYER_CARD_WIDTH
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlayerCard(playerViewModel: PlayerViewModel) {
    val isActive by playerViewModel.isActive.collectAsState()
    val playerColor by playerViewModel.color.collectAsState()
    val money by playerViewModel.money.collectAsState()

    val showPaymentGif = remember { mutableStateOf(false) }
    val gifPainter = remember { mutableStateOf<Int?>(null) }

    val previousMoney = remember { mutableIntStateOf(money) }
    val targetBackgroundColor = remember { mutableStateOf(Color.White) }

    // Animation for the background color change with 50% opacity for red and green
    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor.value,
        animationSpec = tween(durationMillis = 700), label = "Animated background color"
    )

    LaunchedEffect(money) {
        if (money < previousMoney.intValue) {
            // Player lost money, animate to lighter red (blend with white)
            targetBackgroundColor.value = Color.Red.copy(alpha = 0.5f).compositeOver(Color.White)
            gifPainter.value = R.drawable.gif_money_payment
            showPaymentGif.value = true
        } else if (money > previousMoney.intValue) {
            // Player gained money, animate to lighter green (blend with white)
            targetBackgroundColor.value = Color.Green.copy(alpha = 0.5f).compositeOver(Color.White)
            gifPainter.value = R.drawable.gif_money_income
            showPaymentGif.value = true
        }

        delay(1300)
        targetBackgroundColor.value = Color.White

        // Hide GIF after animation
        showPaymentGif.value = false
        previousMoney.intValue = money
    }

    Box(
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp * PLAYER_CARD_WIDTH).dp)
            .height(60.dp)
            .border(
                width = 2.5.dp,
                color = if (isActive) Color.Red else lerp(playerColor, Color.Black, 0.2f),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = animatedBackgroundColor.copy(alpha = 0.9f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left-aligned content
                    Box(modifier = Modifier.weight(1f)) {
                        Text(
                            text = playerViewModel.name.value,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    // Right-aligned content
                    Box(modifier = Modifier.size(((LocalConfiguration.current.screenWidthDp * PLAYER_CARD_WIDTH) * 0.4).dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Money text
                                Text(
                                    text = money.toString(),
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )

                                // Coin image
                                Image(
                                    painter = painterResource(R.drawable.coin),
                                    contentDescription = "Coin Icon",
                                    modifier = Modifier.size(((LocalConfiguration.current.screenWidthDp * PLAYER_CARD_WIDTH) * 0.13).dp)
                                )
                            }
                        }

                        // GIF overlay - Positioned over money and coin icon
                        if (showPaymentGif.value && gifPainter.value != null) {
                            Image(
                                painter = rememberDrawablePainter(
                                    drawable = LocalContext.current.let { context ->
                                        ContextCompat.getDrawable(context, gifPainter.value!!)
                                    }
                                ),
                                contentDescription = "Payment Animation",
                                modifier = Modifier
                                    .size(((LocalConfiguration.current.screenWidthDp * PLAYER_CARD_WIDTH) * 0.3).dp)
                                    .align(Alignment.Center), // Centered over money and coin
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}