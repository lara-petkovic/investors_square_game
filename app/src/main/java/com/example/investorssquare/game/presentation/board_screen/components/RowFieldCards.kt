package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType

@Composable
fun RowFieldCards(
    fieldHeight: Dp,
    fieldWidth: Dp,
    modifier: Modifier = Modifier,
    startIndex: Int,
    board: Board,
    onFieldClick: (Field) -> Unit,
    rotation: Float = 0f,
    translationX: Float = 0f,
    translationY: Float = 0f
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(fieldHeight)
            .padding(
                start = fieldHeight,
                end = fieldHeight
            )
            .graphicsLayer(rotationZ = rotation, translationX = translationX, translationY = translationY),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..9) {
            val index = 10 - i + startIndex
            val field: Field = board.fields[index]
            FieldCard(
                fieldWidth,
                fieldHeight,
                field,
                Modifier.clickable {
                    if (field.type == FieldType.PROPERTY || field.type == FieldType.STATION || field.type == FieldType.UTILITY) {
                        onFieldClick(field)
                    }
                }
            )
        }
    }
}
