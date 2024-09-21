package com.example.investorssquare.game.presentation.board_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardVMEvent
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel
import com.example.investorssquare.util.Constants.FIELD_CARD_STRAP_HEIGHT_PERCENTAGE

@Composable
fun FieldCard(
    fieldWidth: Dp,
    fieldHeight: Dp,
    field: Field,
    modifier: Modifier = Modifier,
    boardViewModel: BoardViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .clickable { boardViewModel.onEvent(BoardVMEvent.OnFieldClicked(field.index)) }
    ) {
        Card(
            modifier = Modifier.size(fieldWidth, fieldHeight),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(width = 1.dp, color = Color.Black)
        ) {
            val property = field as? Property

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        if(boardViewModel.getEstateByFieldIndex(field.index) != null
                            && boardViewModel.getEstateByFieldIndex(field.index)?.ownerIndex?.value != -1) 0.5f else 0.0f)
                    .background(color = Color(0xFFDAF7DB))
                )
                if (field.type == FieldType.PROPERTY) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(fieldHeight * FIELD_CARD_STRAP_HEIGHT_PERCENTAGE)
                            .background(property?.setColor ?: Color.Gray)
                            .align(Alignment.TopStart)
                            .drawBehind {
                                val strokeWidth = 1.5.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Color.Black,
                                    start = androidx.compose.ui.geometry.Offset(0f, y),
                                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            }
                    )
                }

                PlayerDrawer(
                    canvasHeight = fieldHeight * (1 + FIELD_CARD_STRAP_HEIGHT_PERCENTAGE),
                    canvasWidth = fieldWidth,
                    boardVM = boardViewModel,
                    fieldIndex = field.index
                )
            }
        }
    }
}