package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.util.Constants

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FieldCard(
        fieldWidth: Dp,
        fieldHeight: Dp,
        field:Field,
        modifier: Modifier,
        playerVM: PlayerViewModel = hiltViewModel()
){
    Card(
        modifier = modifier.size(fieldWidth, fieldHeight),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        val property = field as? Property
        var cardHeight = fieldHeight

        Box(modifier = Modifier.fillMaxSize()) {
            if (field.type == FieldType.PROPERTY) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight * Constants.FIELD_CARD_STRAP_HEIGHT_PERCENTAGE)
                        .background(property?.setColor ?: Color.Gray)
                        .align(Alignment.TopStart)
                )
            }

            val playersOnField = playerVM.players.value.filter { it.position == field.index }

            PlayerDrawer(
                canvasHeight = fieldHeight * (1 + Constants.FIELD_CARD_STRAP_HEIGHT_PERCENTAGE),
                canvasWidth = fieldWidth,
                players = playersOnField
            )
        }
    }
}