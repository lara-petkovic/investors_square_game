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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants.FIELD_CARD_STRAP_HEIGHT_PERCENTAGE
import kotlinx.coroutines.launch

@Composable
fun FieldCard(
    fieldWidth: Dp,
    fieldHeight: Dp,
    field: Field,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .clickable { coroutineScope.launch { EventBus.postEvent(Event.ON_FIELD_CLICKED(field.index)) } }
    ) {
        Card(
            modifier = Modifier.size(fieldWidth, fieldHeight),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            FieldCardContent(field = field, fieldWidth = fieldWidth, fieldHeight = fieldHeight)
        }
    }
}

@Composable
private fun FieldCardContent(
    field: Field,
    fieldWidth: Dp,
    fieldHeight: Dp
) {
    val isFieldOwned = Game.getEstateByFieldIndex(field.index)?.ownerIndex?.value != -1

    Box(modifier = Modifier.fillMaxSize()) {
        OwnershipIndicator(isOwned = isFieldOwned)
        if (field.type == FieldType.PROPERTY) {
            PropertyStrap(
                property = field as? Property,
                fieldHeight = fieldHeight,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        PlayerDrawer(
            canvasHeight = fieldHeight * (1 + FIELD_CARD_STRAP_HEIGHT_PERCENTAGE),
            canvasWidth = fieldWidth,
            fieldIndex = field.index
        )
    }
}

@Composable
private fun PropertyStrap(
    property: Property?,
    fieldHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(fieldHeight * FIELD_CARD_STRAP_HEIGHT_PERCENTAGE)
            .background(property?.setColor ?: Color.Gray)
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

@Composable
private fun OwnershipIndicator(isOwned: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(if (isOwned) 0.5f else 0.0f)
            .background(Color(0xFFDAF7DB))
    )
}