package com.example.investorssquare.game.presentation.board_screen.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import kotlinx.coroutines.launch

@Composable
fun FinishButton() {
    val coroutineScope = rememberCoroutineScope()
    val isFinishButtonVisible by Game.isFinishButtonVisible.collectAsState()

    if (isFinishButtonVisible) {
        Column {
            Button(
                onClick = {
                    coroutineScope.launch { EventBus.postEvent(Event.ON_MOVE_FINISHED) }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(text = "Finish Turn")
            }
            //privremeno ovde ubaceno dugme za build, samo ga treba premestiti
            //zbog mesta gde sam ga ubacio ovo dugme se prikazuje samo kad neko zavrsava potez ali to naravno treba promeniti da je uvek vidljivo

            Row{Button(
                onClick = {
                    coroutineScope.launch { EventBus.postEvent(Event.ON_SWITCH_TO_BUILDING_MODE) }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(text = "Build")
            }
            Button(
                onClick = {
                    coroutineScope.launch { EventBus.postEvent(Event.ON_SWITCH_TO_SELLING_MODE) }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(text = "Sell")
            }}
        }
    }
}