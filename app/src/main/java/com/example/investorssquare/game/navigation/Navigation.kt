package com.example.investorssquare.game.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.game.presentation.board_screen.BoardScreen
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel

@Composable
fun Navigation(board: Board) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BoardScreen.route) {
        composable(route = Screen.BoardScreen.route) {

            val playerViewModel: PlayerViewModel = hiltViewModel()

            playerViewModel.setPlayers(listOf(
                Player("Lara", 1500,board.playerColors[0]),
                Player("Dusan", 1300, board.playerColors[1]),
                Player("Kornelije", 1200, board.playerColors[2]),
                Player("Jelly", 1100, board.playerColors[3]),
                Player("Luka", 1400, board.playerColors[4]),
                Player("Uros", 1250, board.playerColors[5])
            ))

            Box {
                BoardScreen(playerViewModel = playerViewModel, board = board)
            }
        }
    }
}