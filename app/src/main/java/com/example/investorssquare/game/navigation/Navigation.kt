package com.example.investorssquare.game.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.BoardScreen
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.EventService

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation(board: Board) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.BoardScreen.route) {
        composable(route = Screen.BoardScreen.route) {
            Game.setPlayers(
                listOf(
                    "Lara",
                    "Dusan",
                    "Kornelije",
                    "Jelly",
                    "Luka",
                    "Uros"
                ),
                listOf(
                    board.playerColors[0],
                    board.playerColors[1],
                    board.playerColors[2],
                    board.playerColors[3],
                    board.playerColors[4],
                    board.playerColors[5]
                ),
                Game.ruleBook.startingCapital
            )
            Game.setBoard(board)
            EventBus
            EventService
            Box {
                BoardScreen()
            }
        }
    }
}