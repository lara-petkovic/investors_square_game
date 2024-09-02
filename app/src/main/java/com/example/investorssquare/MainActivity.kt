package com.example.investorssquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalContext
import com.example.investorssquare.game.data.local.JsonParser
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Player
import com.example.investorssquare.game.navigation.Navigation
import com.example.investorssquare.ui.theme.InvestorsSquareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestorsSquareTheme {
                Box {
                    val jsonParser = JsonParser(context = LocalContext.current)
                    val board: Board = jsonParser.loadBoard("table_prototype.json")
                    val players: List<Player> = listOf(
                        Player("Lara", 1500, emptyList()),
                        Player("Dusan", 1300, emptyList()),
                        Player("Kornelije", 1200, emptyList()),
                        Player("Jelly", 1100, emptyList()),
                        Player("Luka002", 1400, emptyList()),
                        Player("Urosh", 1250, emptyList())
                    )

                    Navigation(players)
                }
            }
        }
    }
}