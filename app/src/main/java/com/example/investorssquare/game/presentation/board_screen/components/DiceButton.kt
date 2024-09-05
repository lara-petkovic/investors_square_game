package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

@Composable
fun DiceButton(playerViewModel: PlayerViewModel = hiltViewModel()) {
    val number1 by playerViewModel.diceNumber1.collectAsState()
    val number2 by playerViewModel.diceNumber2.collectAsState()
    val isDiceButtonEnabled by playerViewModel.isDiceButtonEnabled.collectAsState()

    Button(
        onClick = {
            playerViewModel.rollDice()
        },
        enabled = isDiceButtonEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = if (isDiceButtonEnabled) Color.Transparent else Color.Gray),
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .size(width = 100.dp, height = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = getDiceImage(number1)),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Image(
                painter = painterResource(id = getDiceImage(number2)),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

private fun getDiceImage(number: Int): Int {
    return when (number) {
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        else -> R.drawable.dice6
    }
}
