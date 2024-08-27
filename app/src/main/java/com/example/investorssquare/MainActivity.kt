package com.example.investorssquare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.investorssquare.ui.theme.InvestorsSquareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestorsSquareTheme {
                    val image = painterResource(R.drawable.board)
                    Box {
                        Image(
                            painter = image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }
        }
    }
}