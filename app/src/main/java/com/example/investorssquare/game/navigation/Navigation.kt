package com.example.investorssquare.game.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.BoardScreen
import com.example.investorssquare.game.presentation.main_screen.MainScreen
import com.example.investorssquare.game.presentation.player_names_screen.PlayerNamesScreen
import com.example.investorssquare.game.presentation.rulebook_screen.RulebookScreen
import com.example.investorssquare.game.service.EventService

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(route = Screen.RulebookScreen.route) {
            RulebookScreen(navController = navController)
        }

        composable(
            route = "${Screen.PlayerNamesScreen.route}/{playerCount}",
            arguments = listOf(navArgument("playerCount") { type = NavType.IntType })
        ) { backStackEntry ->
            val playerCount = backStackEntry.arguments?.getInt("playerCount") ?: 2
            EventBus
            EventService
            PlayerNamesScreen(navController = navController, playerCount = playerCount)
        }

        composable(route = Screen.BoardScreen.route) {
            BoardScreen(navController = navController)
        }
    }
}