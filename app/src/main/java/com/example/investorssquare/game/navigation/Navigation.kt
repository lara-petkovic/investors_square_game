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
import com.example.investorssquare.game.presentation.board_screen.PlayerViewModel

@Composable
fun Navigation(players: List<Player>, board: Board) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BoardScreen.route) {
        composable(route = Screen.BoardScreen.route) {

            val playerViewModel: PlayerViewModel = hiltViewModel()

            playerViewModel.setPlayers(players)

            Box {
                BoardScreen(playerViewModel = playerViewModel, board = board)
            }
        }
    }
}