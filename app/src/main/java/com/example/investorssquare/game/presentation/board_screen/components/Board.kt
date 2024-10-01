package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus.postEvent
import com.example.investorssquare.game.presentation.board_screen.popups.CommunityCardPopup
import com.example.investorssquare.game.presentation.board_screen.popups.PropertyDetails
import com.example.investorssquare.game.presentation.board_screen.popups.StationDetails
import com.example.investorssquare.game.presentation.board_screen.popups.UtilityDetails
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.util.Constants.BLACK_OVERLAY
import com.example.investorssquare.util.Constants.FIELDS_PER_ROW
import com.example.investorssquare.util.Constants.NUMBER_OF_FIELDS
import com.example.investorssquare.util.Constants.RELATIVE_FIELD_HEIGHT
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition", "DiscouragedApi")
@Composable
fun Board(
    screenWidthDp: Dp,
    sideMargin: Dp,
) {
    val showPopup by Game.showPopup.collectAsState()
    val estates by Game.estates.collectAsState()
    val currentField by Game.currentField.collectAsState()
    val boardSize = (screenWidthDp.value - sideMargin.value * 2).dp
    val fieldHeight = (boardSize.value * RELATIVE_FIELD_HEIGHT).dp
    val fieldWidth = ((boardSize.value - 2 * fieldHeight.value) / FIELDS_PER_ROW).dp
    var centerOfTheBoard by remember { mutableStateOf(IntOffset.Zero) }
    val context = LocalContext.current
    val board = Game.board.value!!
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier.size(boardSize)
    ) {
        val boardWidth = constraints.maxWidth
        val boardHeight = constraints.maxHeight
        val cornerFieldIndices = listOf(0, 10, 20, 30)

        centerOfTheBoard = IntOffset(
            x = boardWidth / 2,
            y = boardHeight / 2
        )

        val isCornerField = currentField?.let { field ->
            cornerFieldIndices.contains(field.index)
        } ?: false

        val isPropertyOwned = estates.any { estate ->
            estate.estate.index == currentField?.index && estate.ownerIndex.value != -1
        }

        if (showPopup && !isCornerField && currentField is Estate && !isPropertyOwned) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = BLACK_OVERLAY))
                    .zIndex(1f)
            )
        }

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

                CornerFieldCard(
                    fieldSize = fieldHeight,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    index = 0
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
                    index = 10
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
                    index = 20
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
                    index = 30
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

                for (estate in estates) {
                    if (estate.ownerIndex.value != -1) {

                        val estateVM = Game.getEstateByFieldIndex(estate.estate.index)
                        val numberOfHouses = estateVM?.numberOfBuildings?.collectAsState()?.value ?: 0

                        Box {
                            BoughtEstateMarker(
                                fieldWidth = fieldWidth,
                                field = estate.estate,
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            x = calculateXOffsetForBoughtEstateMarker(estate.estate.index, fieldHeight, fieldWidth, boardSize).roundToPx(),
                                            y = calculateYOffsetForBoughtEstateMarker(estate.estate.index, fieldHeight, fieldWidth, boardSize).roundToPx()
                                        )
                                    }
                                    .zIndex(1f),
                                horizontal = estate.estate.index < 10 || (estate.estate.index in 21..29)
                            )

                            var xHouseOffset = 1.dp
                            var yHouseOffset = 5.dp
                            when (estate.estate.index) {
                                in 11..19 -> {

                                }
                                in 21..29 -> {
                                }
                                else -> {
                                }
                            }



                            HousesLayout(
                                numberOfHouses = numberOfHouses,
                                houseSize = 10.dp,
                                modifier = Modifier
                                    .offset {
                                        IntOffset(
                                            x = (calculateXOffsetForBoughtEstateMarker(estate.estate.index, fieldHeight, fieldWidth, boardSize) + xHouseOffset).roundToPx(),
                                            y = (calculateYOffsetForBoughtEstateMarker(estate.estate.index, fieldHeight, fieldWidth, boardSize) + yHouseOffset).roundToPx()
                                        )
                                    }
                                    .zIndex(2f)
                            )
                        }
                    }
                }
            }
        }

        currentField?.let { currentField ->
            if (showPopup) {
                currentField.let { field ->
                    val activePlayer = Game.getActivePlayer()
                    val buyButtonVisibility = activePlayer?.let { canBuyEstate(it, field) } ?: false

                    when (field.type) {
                        FieldType.PROPERTY -> PropertyDetails(
                            field = field,
                            onDismissRequest = { Game.dismissPopup() },
                            offset = IntOffset(
                                (fieldHeight + 1.75 * fieldWidth).value.toInt(),
                                (fieldHeight + 0.02f * 9 * fieldWidth).value.toInt()
                            ),
                            popupWidth = (5.5 * fieldWidth.value).dp,
                            popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                            buyButtonVisibility = buyButtonVisibility
                        )

                        FieldType.STATION -> StationDetails(
                            field = field,
                            onDismissRequest = { Game.dismissPopup() },
                            offset = IntOffset(
                                (fieldHeight + 1.75 * fieldWidth).value.toInt(),
                                (fieldHeight + 0.02f * 9 * fieldWidth).value.toInt()
                            ),
                            popupWidth = (5.5 * fieldWidth.value).dp,
                            popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                            buyButtonVisibility = buyButtonVisibility
                        )

                        FieldType.UTILITY -> UtilityDetails(
                            field = field,
                            onDismissRequest = { Game.dismissPopup() },
                            offset = IntOffset(
                                (fieldHeight + 1.75 * fieldWidth).value.toInt(),
                                (fieldHeight + 0.02f * 9 * fieldWidth).value.toInt()
                            ),
                            popupWidth = (5.5 * fieldWidth.value).dp,
                            popupHeight = (0.96 * 9 * fieldWidth.value).dp,
                            buyButtonVisibility = buyButtonVisibility
                        )

                        FieldType.CHANCE, FieldType.COMMUNITY_CHEST -> CommunityCardPopup(
                            isChance = field.type == FieldType.CHANCE,
                            onDismissRequest = {
                                Game.dismissPopup()
                                coroutineScope.launch{ postEvent(Event.ON_COMMUNITY_CARD_CLOSED) }
                            },
                            offset = IntOffset(
                                (fieldHeight + 0.05f * 9 * fieldWidth).value.toInt(),
                                (fieldHeight + 1.5 * fieldWidth).value.toInt()
                            ),
                            popupWidth = (0.9 * 9 * fieldWidth.value).dp,
                            popupHeight = (6 * fieldWidth.value).dp,
                        )
                        else -> {}
                    }
                }
            }
        }
    }
}

private fun canBuyEstate(activePlayer: PlayerViewModel, currentField: Field): Boolean {
    if (currentField !is Estate) {
        return false
    }

    // Check if the player is on the correct field and does not already own this estate
    val isOnCorrectField = activePlayer.position.value == currentField.index
    val alreadyOwnsEstate = activePlayer.estates.value.any { it.estate.index == currentField.index }

    return isOnCorrectField && !alreadyOwnsEstate
}

private fun calculateXOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return when {
        fieldIndex < 10 -> boardSize - (fieldHeight.value + (fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 20 -> boardSize - (fieldHeight.value + 8.5 * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 30 -> boardSize - (fieldHeight.value + (30 - fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        else -> boardSize - fieldHeight - (0.25 * fieldWidth.value).dp
    }
}

private fun calculateYOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return when {
        fieldIndex < 10 -> boardSize - fieldHeight - (0.25 * fieldWidth.value).dp
        fieldIndex < 20 -> boardSize - (fieldHeight.value + (fieldIndex - 11 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 30 -> boardSize - (fieldHeight.value + 8.5 * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        else -> boardSize - (fieldHeight.value + (NUMBER_OF_FIELDS - fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
    }
}