package com.example.investorssquare.game.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main-screen")
    object BoardScreen : Screen("board-screen")

    fun withArgs(vararg args: String) : String {
        return buildString {
            append(route)
            args.forEach {arg ->
                append("/$arg")
            }
        }
    }
}