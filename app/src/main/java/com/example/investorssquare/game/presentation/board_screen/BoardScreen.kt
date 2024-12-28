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
import com.example.investorssquare.R
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.navigation.Screen
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.game.presentation.board_screen.components.buttons.ActionButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.DiceButton
import com.example.investorssquare.game.presentation.board_screen.components.buttons.FinishButton
import com.example.investorssquare.game.presentation.board_screen.popups.PaymentPopupCard
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants.SIDE_BOARD_MARGIN
import com.example.investorssquare.util.Constants.TOP_BOARD_MARGIN

@Composable
fun BoardScreen(navController: NavController) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * TOP_BOARD_MARGIN).dp

    val showPaymentPopup by Game.showPaymentPopup.collectAsState()
    val paymentDetails by Game.paymentDetails.collectAsState()

    var activeButtonId by remember { mutableStateOf<Int?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Back press handling
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
                ActionButton(
                    iconResourceId = R.drawable.icon_build,
                    isButtonClicked = activeButtonId == 1,
                    onButtonClicked = { activeButtonId = if (activeButtonId == 1) null else 1 },
                    event = Event.ON_SWITCH_TO_BUILDING_MODE
                )

                ActionButton(
                    iconResourceId = R.drawable.icon_sell,
                    isButtonClicked = activeButtonId == 2,
                    onButtonClicked = { activeButtonId = if (activeButtonId == 2) null else 2 },
                    event = Event.ON_SWITCH_TO_SELLING_BUILDING_MODE
                )

                ActionButton(
                    iconResourceId = R.drawable.icon_mortgage,
                    isButtonClicked = activeButtonId == 3,
                    onButtonClicked = { activeButtonId = if (activeButtonId == 3) null else 3 },
                    event = Event.ON_SWITCH_TO_MORTGAGE_MODE
                )

                ActionButton(
                    iconResourceId = R.drawable.icon_redeem,
                    isButtonClicked = activeButtonId == 4,
                    onButtonClicked = { activeButtonId = if (activeButtonId == 4) null else 4 },
                    event = Event.ON_SWITCH_TO_REDEEM_MODE
                )

                ActionButton(
                    iconResourceId = R.drawable.icon_redeem, //TODO LARA: make an image for this
                    isButtonClicked = activeButtonId == 5,
                    onButtonClicked = { activeButtonId = if (activeButtonId == 5) null else 5 },
                    event = Event.ON_SWITCH_TO_SELLING_PROPERTY_MODE
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