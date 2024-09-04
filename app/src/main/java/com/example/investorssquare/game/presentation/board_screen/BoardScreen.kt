package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.R
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.util.Constants

@Composable
fun BoardScreen(
    playerViewModel: PlayerViewModel = hiltViewModel(),
    board: Board
) {
    val players by playerViewModel.players.collectAsState()
    val activePlayerIndex by playerViewModel.activePlayerIndex.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * Constants.SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * Constants.TOP_BOARD_MARGIN).dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sideMargin, end = sideMargin, top = topMargin),
        verticalArrangement = Arrangement.Top
    ) {
        Board(screenWidthDp, sideMargin, board)
        PlayerCardColumns(
            players = players,
            screenWidthDp = screenWidthDp.value.toInt(),
            activePlayerIndex = activePlayerIndex
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DiceButton()

            DiceButton()
        }
    }
}

@Composable
private fun DiceButton() {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Transparent to let the image show
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.size(40.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dice1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
