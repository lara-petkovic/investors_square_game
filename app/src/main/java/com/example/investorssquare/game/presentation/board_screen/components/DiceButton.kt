package com.example.investorssquare.game.presentation.board_screen.components

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel
import kotlinx.coroutines.delay

@Composable
fun DiceButton(boardViewModel: BoardViewModel = hiltViewModel()) {
    val number1 by boardViewModel.diceViewModel.diceNumber1.collectAsState()
    val number2 by boardViewModel.diceViewModel.diceNumber2.collectAsState()
    val isDiceButtonEnabled by boardViewModel.diceViewModel.isDiceButtonEnabled.collectAsState()

    var currentDice1 by remember { mutableIntStateOf(number1) }
    var currentDice2 by remember { mutableIntStateOf(number2) }
    var isRolling by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.dice_sound_effect) }

    Button(
        onClick = {
            isRolling = true
            boardViewModel.rollDice()
            mediaPlayer.start() // Play sound effect when dice are rolled
        },
        enabled = isDiceButtonEnabled && !isRolling,
        colors = ButtonDefaults.buttonColors(containerColor = if (isDiceButtonEnabled) Color.Transparent else Color.Gray),
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(width = 100.dp, height = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            DiceImage(diceNumber = currentDice1)
            DiceImage(diceNumber = currentDice2)
        }
    }

    // Dice roll animation
    LaunchedEffect(isRolling) {
        if (isRolling) {
            val diceNumbers = (1..6).toList()
            val animationDuration = 1000L

            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < animationDuration) {
                currentDice1 = diceNumbers.random()
                currentDice2 = diceNumbers.random()
                delay(20)
            }

            currentDice1 = number1
            currentDice2 = number2
            boardViewModel.moveActivePlayer()

            isRolling = false
        }
    }
}

@Composable
fun DiceImage(diceNumber: Int) {
    val painterResourceId = getDiceImage(diceNumber)
    Image(
        painter = painterResource(id = painterResourceId),
        contentDescription = null,
        modifier = Modifier.size(40.dp)
    )
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