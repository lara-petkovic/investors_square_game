package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel

@Composable
fun CornerFieldCard(
    fieldSize: Dp,
    modifier: Modifier = Modifier,
    index: Int,
    playerVM: BoardViewModel = hiltViewModel()
) {
    Card(
        modifier = modifier
            .size(fieldSize),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        PlayerDrawer(
            canvasHeight = fieldSize,
            canvasWidth = fieldSize,
            boardVM = playerVM,
            fieldIndex = index
        )
    }
}