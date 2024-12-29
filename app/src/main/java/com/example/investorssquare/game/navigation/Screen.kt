package com.example.investorssquare.game.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object RulebookScreen : Screen("rulebook_screen")
    object PlayerNamesScreen : Screen("player_names_screen")
    object BoardScreen : Screen("board_screen")
}