package com.example.investorssquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import com.example.investorssquare.game.navigation.Navigation
import com.example.investorssquare.ui.theme.InvestorsSquareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InvestorsSquareTheme {
                Box {
                    Navigation()
                }
            }
        }
    }
}