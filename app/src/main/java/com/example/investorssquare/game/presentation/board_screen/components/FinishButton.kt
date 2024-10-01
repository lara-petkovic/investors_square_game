package com.example.investorssquare.game.presentation.board_screen.components

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
        Button(
            onClick = {
                coroutineScope.launch { EventBus.postEvent(Event.ON_MOVE_FINISHED) }
            },
            modifier = Modifier
                .padding(top = 10.dp)
        ) {
            Text(text = "Finish Turn")
        }
    }
}