package com.example.investorssquare.game.presentation.rulebook_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RulebookScreen(navController: NavController) {
    var rules by remember {
        mutableStateOf(
            mapOf(
                "Rule 1: No cheating" to true,
                "Rule 2: Maximum 6 players" to true,
                "Rule 3: Can mortgage properties" to false
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Game Rules", style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp))

        Spacer(modifier = Modifier.height(16.dp))

        rules.forEach { (rule, isChecked) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        rules = rules.toMutableMap().apply {
                            this[rule] = checked
                        }
                    }
                )
                Text(rule)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Main")
        }
    }
}
