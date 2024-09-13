package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel

@Composable
fun BoughtEstateMarker(
    fieldWidth: Dp,
    field: Field,
    modifier: Modifier,
    boardViewModel: BoardViewModel,
    horizontal: Boolean = true
) {
    val playerVM = boardViewModel.getOwnerOfEstate(field.index)
    val playerColor = playerVM?.color?.collectAsState()?.value ?: Color.Gray

    val width = if (horizontal) fieldWidth / 2 else fieldWidth / 3.7f
    val height = if (horizontal) fieldWidth / 3.7f else fieldWidth / 2

    Box(
        modifier = modifier
            .background(playerColor)
            .size(width, height)
            .border(1.dp, color = Color.Black)
    )
}