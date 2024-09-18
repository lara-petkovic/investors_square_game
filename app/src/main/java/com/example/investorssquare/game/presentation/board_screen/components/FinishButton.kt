package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel

@Composable
fun FinishButton(boardViewModel: BoardViewModel = hiltViewModel()) {
    val isFinishButtonVisible by boardViewModel.isFinishButtonVisible.collectAsState()

    if (isFinishButtonVisible) {
        Button(
            onClick = {
                boardViewModel.finishTurn()
            },
            modifier = Modifier
                .padding(top = 10.dp)
        ) {
            Text(text = "Finish Turn")
        }
    }
}