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
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.PaymentDetails
import com.example.investorssquare.util.Constants.BLACK_OVERLAY
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
@Preview(showBackground = true)
fun PaymentPopupCardPreview() { // Mocked data for the preview of the popup card
    val payer = PlayerViewModel().apply {
        setName("Lara")
        setColor(Color.Red)
        setMoney(1500)
        setIndex(0)
    }

    val receiver = PlayerViewModel().apply {
        setName("Menza")
        setColor(Color.Blue)
        setMoney(1500)
        setIndex(1)
    }
    val paymentDetails = PaymentDetails(payer, receiver, amount = 62)

    PaymentPopupCard(paymentDetails = paymentDetails, onDismiss = {})
}

@Composable
fun PaymentPopupCard(
    paymentDetails: PaymentDetails?, onDismiss: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = BLACK_OVERLAY))
            .clickable { onDismiss() }
    ) {
        if (paymentDetails != null) {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .height(120.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ){
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PlayerNameAndImage(paymentDetails.payer, Modifier.weight(1f))

                    Arrow(paymentDetails, Modifier.weight(0.5f))

                    PlayerNameAndImage(paymentDetails.receiver, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun Arrow(paymentDetails: PaymentDetails, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        Text(text = "$${paymentDetails.amount}", color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
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
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun PlayerNameAndImage(playerVM: PlayerViewModel, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = imageByColor(playerVM)),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .background(
                    Color.Gray.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(30.dp)
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = playerVM.name.value)
    }
}

private fun imageByColor(playerViewModel: PlayerViewModel): Int {
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