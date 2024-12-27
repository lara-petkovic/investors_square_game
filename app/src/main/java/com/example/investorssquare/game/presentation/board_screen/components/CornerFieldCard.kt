package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.BuildingService
import com.example.investorssquare.game.service.SellingService
import kotlinx.coroutines.launch

@Composable
fun CornerFieldCard(
    fieldSize: Dp,
    modifier: Modifier = Modifier,
    index: Int,
) {
    val highlightModeOn = Game.highlightMode.collectAsState()
    Card(
        modifier = modifier
            .size(fieldSize),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Box(
            modifier = modifier
                .background(
                    if(highlightModeOn.value)
                        Color(0f, 0f, 0f, 0.5f)
                    else Color.Transparent
                )
        ) {
            PlayerDrawer(
                canvasHeight = fieldSize,
                canvasWidth = fieldSize,
                fieldIndex = index
            )
        }
    }
}