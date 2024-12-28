package com.example.investorssquare.game.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.BoardScreen
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.main_screen.MainScreen
import com.example.investorssquare.game.service.EventService

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val temp_players = listOf(
                "Player 1",
                "Player 2",
                "Player 3",
                "Player 4",
                "Player 5",
                "Player 6"
            )

            MainScreen(
                navController = navController,
                onGameStart = { players, selectedBoard ->
                    navController.navigate(Screen.BoardScreen.route) {
                        Game.setPlayers(
                            temp_players, //TODO: players,
                            selectedBoard.playerColors,
                            Game.ruleBook.startingCapital
                        )
                        Game.setBoard(selectedBoard)
                        EventBus
                        EventService
                    }
                }
            )
        }

        composable(route = Screen.BoardScreen.route) {
            BoardScreen(navController = navController)
        }
    }
}