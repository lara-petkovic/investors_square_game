package com.example.investorssquare.game.presentation.main_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.investorssquare.game.data.local.JsonParser
import com.example.investorssquare.game.domain.model.Board

@Composable
fun MainScreen(
    navController: NavController,
    onGameStart: (List<String>, Board) -> Unit
) {
    val jsonParser = JsonParser(context = navController.context)

    var player1 by remember { mutableStateOf("") }
    var player2 by remember { mutableStateOf("") }
    var player3 by remember { mutableStateOf("") }
    var player4 by remember { mutableStateOf("") }
    var player5 by remember { mutableStateOf("") }
    var player6 by remember { mutableStateOf("") }

    var selectedBoard: Board? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = player1,
                onValueChange = { player1 = it },
                label = { Text("Player 1") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player2,
                onValueChange = { player2 = it },
                label = { Text("Player 2") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player3,
                onValueChange = { player3 = it },
                label = { Text("Player 3") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player4,
                onValueChange = { player4 = it },
                label = { Text("Player 4") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player5,
                onValueChange = { player5 = it },
                label = { Text("Player 5") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = player6,
                onValueChange = { player6 = it },
                label = { Text("Player 6") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            val isFormValid = player1.isNotEmpty() && player2.isNotEmpty() && player3.isNotEmpty() &&
                    player4.isNotEmpty() && player5.isNotEmpty() && player6.isNotEmpty()

            Button(
                onClick = {
                    // Load the selected board
                    selectedBoard = jsonParser.loadBoard("board_default.json")

                    selectedBoard?.let {
                        onGameStart(
                            listOf(player1, player2, player3, player4, player5, player6),
                            it
                        )
                    }
                },
                enabled = true//TODO: isFormValid
            ) {
                Text("Start Game")
            }
        }
    }
}