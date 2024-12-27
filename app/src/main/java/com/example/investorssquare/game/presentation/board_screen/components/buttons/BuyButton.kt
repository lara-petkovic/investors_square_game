package com.example.investorssquare.game.presentation.board_screen.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.util.Constants.BUY

@Composable
fun BuyButton(popupWidth: Dp, popupHeight: Dp, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        onClick = onClick,
        modifier = Modifier
            .size((popupWidth.value * 0.5).dp, (popupHeight.value * 0.1).dp)
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(BUY, color = Color.White, style = MaterialTheme.typography.labelSmall)
    }
    Spacer(modifier = Modifier.height(3.dp))
}