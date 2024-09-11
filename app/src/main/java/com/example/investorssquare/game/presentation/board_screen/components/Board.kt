package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.presentation.board_screen.popups.CommunityCardPopup
import com.example.investorssquare.game.presentation.board_screen.popups.PaymentPopupCard
import com.example.investorssquare.game.presentation.board_screen.popups.PropertyDetails
import com.example.investorssquare.game.presentation.board_screen.popups.StationDetails
import com.example.investorssquare.game.presentation.board_screen.popups.UtilityDetails
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel
import com.example.investorssquare.util.Constants.FIELDS_PER_ROW
import com.example.investorssquare.util.Constants.NUMBER_OF_FIELDS
import com.example.investorssquare.util.Constants.RELATIVE_FIELD_HEIGHT
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition", "DiscouragedApi")
@Composable
fun Board(
    screenWidthDp: Dp,
    sideMargin: Dp,
    board: Board,
    boardViewModel: BoardViewModel
) {
    val showPopup by boardViewModel.showPopup.collectAsState()
    val estates by boardViewModel.estates.collectAsState()
    val currentField by boardViewModel.currentField.collectAsState()
    val boardSize = (screenWidthDp.value - sideMargin.value * 2).dp
    val fieldHeight = (boardSize.value * RELATIVE_FIELD_HEIGHT).dp
    val fieldWidth = ((boardSize.value - 2 * fieldHeight.value) / FIELDS_PER_ROW).dp
    var centerOfTheBoard by remember { mutableStateOf(IntOffset.Zero) }
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = Modifier.size(boardSize)
    ) {
        val boardWidth = constraints.maxWidth
        val boardHeight = constraints.maxHeight
        centerOfTheBoard = IntOffset(
            x = boardWidth / 2,
            y = boardHeight / 2
        )
        Card(
            modifier = Modifier.size(boardSize),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(width = 2.dp, color = Color.Black)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (board.imageUrl.isNotEmpty()) {
                    Image(
                        painter = painterResource(context.resources.getIdentifier(board.imageUrl, "drawable", context.packageName)),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                for(estate in estates){
                    if(estate.ownerIndex.value != -1)
                        BoughtEstateMarker(
                            fieldWidth = fieldWidth,
                            field = estate.estate.value,
                            modifier = Modifier.offset {
                                IntOffset(
                                    x = calculateXOffsetForBoughtEstateMarker(estate.estate.value.index, fieldHeight, fieldWidth, boardSize).roundToPx(),
                                    y = calculateYOffsetForBoughtEstateMarker(estate.estate.value.index, fieldHeight, fieldWidth, boardSize).roundToPx()
                                )
                            },
                            boardViewModel = boardViewModel,
                            horizontal = estate.estate.value.index<10 || (estate.estate.value.index in 21..29)
                        )
                }
                CornerFieldCard(
                    fieldSize = fieldHeight,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    index = 0,
                    board = board,
                    onFieldClick = { }
                )
                RowFieldCards(
                    fieldHeight = fieldHeight,
                    fieldWidth = fieldWidth,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    startIndex = 0,
                    board = board,
                    onFieldClick = { }
                )
                CornerFieldCard(
                    fieldSize = fieldHeight,
                    modifier = Modifier.align(Alignment.BottomStart),
                    index = 10,
                    board = board,
                    onFieldClick = { }
                )
                RowFieldCards(
                    fieldHeight = fieldHeight,
                    fieldWidth = fieldWidth,
                    modifier = Modifier.align(Alignment.BottomStart),
                    startIndex = 10,
                    board = board,
                    onFieldClick = { },
                    rotation = 90f,
                    translationY = -with(LocalDensity.current) { fieldWidth.toPx() } * 4.5f - with(LocalDensity.current){fieldHeight.toPx()} * 0.5f,
                    translationX = -with(LocalDensity.current){fieldHeight.toPx()} * 2.5f,

                )
                CornerFieldCard(
                    fieldSize = fieldHeight,
                    modifier = Modifier.align(Alignment.TopStart),
                    index = 20,
                    board = board,
                    onFieldClick = { }
                )
                RowFieldCards(
                    fieldHeight = fieldHeight,
                    fieldWidth = fieldWidth,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    startIndex = 20,
                    board = board,
                    onFieldClick = { },
                    rotation = 180f,
                    translationY = -with(LocalDensity.current) { fieldWidth.toPx() } * 9.0f - with(LocalDensity.current){fieldHeight.toPx()} * 1f,
                )
                CornerFieldCard(
                    fieldSize = fieldHeight,
                    modifier = Modifier.align(Alignment.TopEnd),
                    index = 30,
                    board = board,
                    onFieldClick = { }
                )
                RowFieldCards(
                    fieldHeight = fieldHeight,
                    fieldWidth = fieldWidth,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    startIndex = 30,
                    board = board,
                    onFieldClick = { },
                    rotation = 270f,
                    translationY = -with(LocalDensity.current) { fieldWidth.toPx() } * 4.5f - with(LocalDensity.current){fieldHeight.toPx()} * 0.5f,
                    translationX = with(LocalDensity.current){fieldHeight.toPx()} * 2.5f,
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(fieldHeight)
                        .onGloballyPositioned { layoutCoordinates ->
                        val size = layoutCoordinates.size
                        val positionInWindow = layoutCoordinates.positionInWindow()
                        val centerX = positionInWindow.x + (size.width / 2)
                        val centerY = positionInWindow.y + (size.height / 2)
                        centerOfTheBoard = IntOffset(centerX.roundToInt(), centerY.roundToInt())
                    },
                    border = BorderStroke(width = 1.dp, color = Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RectangleShape
                ){}
            }
        }

        currentField?.let { currentField ->
            if (showPopup) {
                if(currentField.type == FieldType.PROPERTY) {
                    PropertyDetails(
                        field = currentField,
                        onDismissRequest = { boardViewModel.dismissPopup()},
                        offset = IntOffset((with(LocalDensity.current) { fieldHeight.toPx() }+1.75*with(LocalDensity.current) { fieldWidth.toPx() }).toInt(),(with(LocalDensity.current) { fieldHeight.toPx() }+(0.02f*9*with(LocalDensity.current) { fieldWidth.toPx() })).toInt()),
                        popupWidth = (5.5 * fieldWidth.value).dp,
                        popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                        boardViewModel = boardViewModel
                    )
                }
                if(currentField.type == FieldType.STATION) {
                    StationDetails(
                        field = currentField,
                        onDismissRequest = { boardViewModel.dismissPopup() },
                        offset = IntOffset((with(LocalDensity.current) { fieldHeight.toPx() }+1.75*with(LocalDensity.current) { fieldWidth.toPx() }).toInt(),(with(LocalDensity.current) { fieldHeight.toPx() }+(0.02f*9*with(LocalDensity.current) { fieldWidth.toPx() })).toInt()),
                        popupWidth = (5.5 * fieldWidth.value).dp,
                        popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                        boardViewModel = boardViewModel
                    )
                }
                if(currentField.type == FieldType.UTILITY) {
                    UtilityDetails(
                        field = currentField,
                        onDismissRequest = { boardViewModel.dismissPopup() },
                        offset = IntOffset((with(LocalDensity.current) { fieldHeight.toPx() }+1.75*with(LocalDensity.current) { fieldWidth.toPx() }).toInt(),(with(LocalDensity.current) { fieldHeight.toPx() }+(0.02f*9*with(LocalDensity.current) { fieldWidth.toPx() })).toInt()),
                        popupWidth = (5.5 * fieldWidth.value).dp,
                        popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                        boardViewModel = boardViewModel
                    )
                }
                if(currentField.type == FieldType.CHANCE || currentField.type == FieldType.COMMUNITY_CHEST) {
                    CommunityCardPopup(
                        isChance = currentField.type==FieldType.CHANCE,
                        onDismissRequest = { boardViewModel.dismissPopup()},
                        offset = IntOffset((with(LocalDensity.current) { fieldHeight.toPx() }+(0.05f*9*with(LocalDensity.current) { fieldWidth.toPx() })).toInt(), (with(LocalDensity.current) { fieldHeight.toPx() }+1.5*with(LocalDensity.current) { fieldWidth.toPx() }).toInt()),
                        popupWidth = (0.9 * 9 * fieldWidth.value).dp,
                        popupHeight = (6 * fieldWidth.value).dp,
                        boardViewModel = boardViewModel
                    )
                }
            }
        }
    }
}

fun calculateXOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return if(fieldIndex < 10)
        boardSize-(fieldHeight.value + (fieldIndex-1+0.25)* fieldWidth.value).dp-(0.5*fieldWidth.value).dp
    else if(fieldIndex < 20)
        boardSize-(fieldHeight.value + 8.5*fieldWidth.value).dp-(0.5*fieldWidth.value).dp
    else if(fieldIndex < 30)
        boardSize-(fieldHeight.value + (30-fieldIndex-1+0.25)* fieldWidth.value).dp-(0.5*fieldWidth.value).dp
    else
        boardSize-fieldHeight-(0.25*fieldWidth.value).dp
}
fun calculateYOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return if(fieldIndex < 10)
        boardSize-fieldHeight-(0.25*fieldWidth.value).dp
    else if(fieldIndex < 20)
        boardSize-(fieldHeight.value + (fieldIndex-11+0.25)* fieldWidth.value).dp-(0.5*fieldWidth.value).dp
    else if(fieldIndex < 30)
        boardSize-(fieldHeight.value + 8.5*fieldWidth.value).dp-(0.5*fieldWidth.value).dp
    else
        boardSize-(fieldHeight.value + (NUMBER_OF_FIELDS-fieldIndex-1+0.25)* fieldWidth.value).dp-(0.5*fieldWidth.value).dp
}