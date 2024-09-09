package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.presentation.board_screen.components.Board
import com.example.investorssquare.game.presentation.board_screen.components.DiceButton
import com.example.investorssquare.game.presentation.board_screen.components.FinishButton
import com.example.investorssquare.game.presentation.board_screen.components.PlayerCardColumns
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel
import com.example.investorssquare.util.Constants.SIDE_BOARD_MARGIN
import com.example.investorssquare.util.Constants.TOP_BOARD_MARGIN

@Composable
fun BoardScreen(
    boardViewModel: BoardViewModel = hiltViewModel(),
    board: Board
) {
    boardViewModel.setBoard(board)
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val sideMargin = (screenWidthDp.value * SIDE_BOARD_MARGIN).dp
    val topMargin = (screenWidthDp.value * TOP_BOARD_MARGIN).dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = sideMargin, end = sideMargin, top = topMargin),
        verticalArrangement = Arrangement.Top
    ) {
        Board(screenWidthDp, sideMargin, board, boardViewModel)

        PlayerCardColumns(boardVM = boardViewModel)

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp)) {
            DiceButton(boardViewModel = boardViewModel)
        }

        Box(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 10.dp)) {
            FinishButton(boardViewModel = boardViewModel)
        }
    }
}