package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun PlayerCard(playerViewModel: PlayerViewModel) {
    val isActive by playerViewModel.isActive.collectAsState()
    val playerColor by playerViewModel.color.collectAsState()
    val money by playerViewModel.money.collectAsState()

    val showPaymentGif = remember { mutableStateOf(false) }
    val gifPainter = remember { mutableStateOf<Int?>(null) }
    val previousMoney = remember { mutableStateOf(money) }
    val targetBackgroundColor = remember { mutableStateOf(Color.White) }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor.value,
        animationSpec = tween(durationMillis = 700), label = "Animated background color"
    )

    LaunchedEffect(money) {
        handleMoneyChange(money, previousMoney.value, targetBackgroundColor, gifPainter, showPaymentGif)
        previousMoney.value = money
    }

    PlayerCardLayout(
        isActive = isActive,
        playerColor = playerColor,
        animatedBackgroundColor = animatedBackgroundColor,
        playerViewModel = playerViewModel,
        money = money,
        showPaymentGif = showPaymentGif.value,
        gifPainter = gifPainter.value
    )
}

@Composable
private fun PlayerCardLayout(
    isActive: Boolean,
    playerColor: Color,
    animatedBackgroundColor: Color,
    playerViewModel: PlayerViewModel,
    money: Int,
    showPaymentGif: Boolean,
    gifPainter: Int?
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val cardWidth = (screenWidthDp * PLAYER_CARD_WIDTH).dp

    Box(
        modifier = Modifier
            .width(cardWidth)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerInfoSection(playerViewModel.name.value, Modifier.weight(1f))
                MoneySection(money, showPaymentGif, gifPainter, cardWidth.value)
            }
        }
    }
}

@Composable
private fun MoneySection(
    money: Int,
    showPaymentGif: Boolean,
    gifPainter: Int?,
    cardWidth: Float
) {
    Box(
        modifier = Modifier
            .size((cardWidth * 0.4f).dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = money.toString(),
                fontSize = 15.sp,
                color = Color.Black
            )

            Image(
                painter = painterResource(R.drawable.coin),
                contentDescription = "Coin Icon",
                modifier = Modifier.size((cardWidth * 0.13f).dp)
            )
        }

        if (showPaymentGif && gifPainter != null) {
            Image(
                painter = rememberDrawablePainter(
                    drawable = LocalContext.current.let { context ->
                        ContextCompat.getDrawable(context, gifPainter)
                    }
                ),
                contentDescription = "Payment Animation",
                modifier = Modifier
                    .size((cardWidth * 0.3f).dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
private fun PlayerInfoSection(playerName: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = playerName,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

private suspend fun handleMoneyChange(
    currentMoney: Int,
    previousMoney: Int,
    targetBackgroundColor: MutableState<Color>,
    gifPainter: MutableState<Int?>,
    showPaymentGif: MutableState<Boolean>
) {
    if (currentMoney < previousMoney) {
        targetBackgroundColor.value = Color.Red.copy(alpha = 0.5f).compositeOver(Color.White)
        gifPainter.value = R.drawable.gif_money_payment
    } else if (currentMoney > previousMoney) {
        targetBackgroundColor.value = Color.Green.copy(alpha = 0.5f).compositeOver(Color.White)
        gifPainter.value = R.drawable.gif_money_income
    }

    showPaymentGif.value = true
    delay(1300)
    targetBackgroundColor.value = Color.White
    showPaymentGif.value = false
}