package com.example.investorssquare.game.presentation.board_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.investorssquare.game.navigation.Screen
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.game.presentation.board_screen.components.buttons.BuildButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.DiceButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.FinishButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.MortgageButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.RedeemButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.SellButton
import com.example.investorssquare.game.presentation.board_screen.popups.PaymentPopupCard
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants.SIDE_BOARD_MARGIN
import com.example.investorssquare.util.Constants.TOP_BOARD_MARGIN

@Composable
fun  BoardScreen(navController: NavController) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * TOP_BOARD_MARGIN).dp

    val showPaymentPopup by Game.showPaymentPopup.collectAsState()
    val paymentDetails by Game.paymentDetails.collectAsState()
    var isBuildButtonClicked by remember { mutableStateOf(false) }
    var isSellButtonClicked by remember { mutableStateOf(false) }
    var isMortgageButtonClicked by remember { mutableStateOf(false) }
    var isRedeemButtonClicked by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // back press handling
    BackHandler {
        showExitDialog = true
    }

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

            Row {
                BuildButton(
                    isButtonClicked = isBuildButtonClicked,
                    onButtonClicked = { isBuildButtonClicked = !isBuildButtonClicked }
                )

                SellButton(
                    isButtonClicked = isSellButtonClicked,
                    onButtonClicked = { isSellButtonClicked = !isSellButtonClicked }
                )

                MortgageButton(
                    isButtonClicked = isMortgageButtonClicked,
                    onButtonClicked = { isMortgageButtonClicked = !isMortgageButtonClicked }
                )

                RedeemButton(
                    isButtonClicked = isRedeemButtonClicked,
                    onButtonClicked = { isRedeemButtonClicked = !isRedeemButtonClicked }
                )
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

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Game") },
            text = { Text("Are you sure you want to exit?") },
            confirmButton = {
                Button(onClick = {
                    showExitDialog = false
                    navController.navigate(Screen.MainScreen.route) {
                        // Clear the back stack to prevent returning to BoardScreen
                        popUpTo(Screen.MainScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}