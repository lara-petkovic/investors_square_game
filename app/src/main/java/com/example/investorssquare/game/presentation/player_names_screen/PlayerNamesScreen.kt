package com.example.investorssquare.game.presentation.player_names_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.investorssquare.R
import com.example.investorssquare.game.navigation.Screen
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PlayerNamesScreen(
    navController: NavController,
    playerCount: Int
) {
    val boardColors = Game.board.value?.playerColors ?: listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta
    )
    val availableColors = remember { mutableStateOf(boardColors.take(6).toMutableList()) }
    val players = remember { mutableStateOf(List(playerCount) { "" }) }
    val playerColors = remember { mutableStateOf(MutableList(playerCount) { Color.Gray }) }

    var showColorPickerForPlayer by remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        players.value.forEachIndexed { index, player ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                TextField(
                    value = player,
                    onValueChange = { newName ->
                        players.value = players.value.toMutableList().apply {
                            this[index] = newName
                        }
                    },
                    label = { Text("Player ${index + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                if (playerColors.value[index] == Color.Gray) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_color_picker),
                        contentDescription = "Pick Color",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { showColorPickerForPlayer = index }
                            .padding(8.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = playerColors.value[index],
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                            .clickable {
                                availableColors.value.add(playerColors.value[index]) // Return the color to the pool
                                playerColors.value[index] = Color.Gray
                                showColorPickerForPlayer = index
                            }
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Game.setPlayers(players.value, playerColors.value, Game.ruleBook.startingCapital)
                navController.navigate(Screen.BoardScreen.route)
            },
            enabled = players.value.all { it.isNotEmpty() } && playerColors.value.all { it != Color.Gray }
        ) {
            Text("Start Game")
        }

        if (showColorPickerForPlayer != -1) {
            ColorPickerPopup(
                colors = availableColors.value,
                onColorSelected = { selectedColor -> // Assign the selected color to a player
                    playerColors.value[showColorPickerForPlayer] = selectedColor
                    availableColors.value.remove(selectedColor)
                    showColorPickerForPlayer = -1
                },
                onDismiss = {
                    showColorPickerForPlayer = -1
                }
            )
        }
    }
}

@Composable
fun ColorPickerPopup(
    colors: List<Color>,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Select a Color", modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color, shape = androidx.compose.foundation.shape.CircleShape)
                                .clickable { onColorSelected(color) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}