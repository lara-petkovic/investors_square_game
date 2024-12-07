package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.DiceButton
import com.example.investorssquare.game.presentation.board_screen.components.FinishButton
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.game.presentation.board_screen.popups.PaymentPopupCard
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants.SIDE_BOARD_MARGIN
import com.example.investorssquare.util.Constants.TOP_BOARD_MARGIN

@Composable
fun BoardScreen() {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * TOP_BOARD_MARGIN).dp

    val showPaymentPopup by Game.showPaymentPopup.collectAsState()
    val paymentDetails by Game.paymentDetails.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = sideMargin, end = sideMargin, top = topMargin),
            verticalArrangement = Arrangement.Top
        ) {
            Board(screenWidthDp, sideMargin)

            PlayerCardColumns()

            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)) {
                DiceButton()
            }

            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)) {
                FinishButton()
            }
        }

        if (showPaymentPopup && paymentDetails != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.Center)
            ) {
                PaymentPopupCard(
                    paymentDetails = paymentDetails,
                    onDismiss = { Game.dismissPaymentPopup() }
                )
            }
        }
    }
}