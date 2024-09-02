package com.example.investorssquare.game.presentation.board_screen.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.util.Constants

@Composable
fun Board(
    screenWidthDp: Dp,
    sideMargin: Dp
) {
    val context = LocalContext.current
    val boardSize = (screenWidthDp.value - sideMargin.value * 2).dp
    val fieldHeight = (boardSize.value * Constants.RELATIVE_FIELD_HEIGHT).dp
    val fieldWidth = ((boardSize.value - 2*fieldHeight.value)/ Constants.FIELDS_PER_ROW).dp

        Box(
            modifier = Modifier.size(boardSize)
        ) {
            Card(
                modifier = Modifier.size(boardSize),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 2.dp, color = Color.Black)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CornerFieldCard(
                        fieldSize = fieldHeight,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        index = 0
                    )
                    HorizontalRowFieldCards(
                        fieldHeight = fieldHeight,
                        fieldWidth = fieldWidth,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        startIndex = 0
                    )
                    CornerFieldCard(
                        fieldSize = fieldHeight,
                        modifier = Modifier.align(Alignment.BottomStart),
                        index = 10
                    )
                    VerticalRowFieldCards(
                        fieldHeight = fieldHeight,
                        fieldWidth = fieldWidth,
                        modifier = Modifier.align(Alignment.CenterStart),
                        startIndex = 10
                    )
                    CornerFieldCard(
                        fieldSize = fieldHeight,
                        modifier = Modifier.align(Alignment.TopStart),
                        index = 20
                    )
                    HorizontalRowFieldCards(
                        fieldHeight = fieldHeight,
                        fieldWidth = fieldWidth,
                        modifier = Modifier.align(Alignment.TopCenter),
                        startIndex = 20
                    )
                    CornerFieldCard(
                        fieldSize = fieldHeight,
                        modifier = Modifier.align(Alignment.TopEnd),
                        index = 30
                    )
                    VerticalRowFieldCards(
                        fieldHeight = fieldHeight,
                        fieldWidth = fieldWidth,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        startIndex = 30
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(fieldHeight),
                        border = BorderStroke(width = 1.dp, color = Color.Black),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RectangleShape
                    ){}
                }
            }
        }

}