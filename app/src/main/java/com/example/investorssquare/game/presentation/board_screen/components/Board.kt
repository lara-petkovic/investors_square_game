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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus.postEvent
import com.example.investorssquare.game.presentation.board_screen.popups.CommunityCardPopup
import com.example.investorssquare.game.presentation.board_screen.popups.DebtPopup
import com.example.investorssquare.game.presentation.board_screen.popups.JailPopup
import com.example.investorssquare.game.presentation.board_screen.popups.PropertyDetails
import com.example.investorssquare.game.presentation.board_screen.popups.StationDetails
import com.example.investorssquare.game.presentation.board_screen.popups.UtilityDetails
import com.example.investorssquare.game.service.BankruptcyService
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.BoardService.currentField
import com.example.investorssquare.game.service.BoardService.dismissPopupForField
import com.example.investorssquare.game.service.BoardService.showPopup
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.util.Constants.BLACK_OVERLAY
import com.example.investorssquare.util.Constants.FIELDS_PER_ROW
import com.example.investorssquare.util.Constants.RELATIVE_COMMUNITY_CARD_HEIGHT
import com.example.investorssquare.util.Constants.RELATIVE_COMMUNITY_CARD_WIDTH
import com.example.investorssquare.util.Constants.RELATIVE_FIELD_HEIGHT
import com.example.investorssquare.util.Constants.RELATIVE_POPUP_HEIGHT
import com.example.investorssquare.util.Constants.RELATIVE_POPUP_WIDTH
import com.example.investorssquare.util.Constants.THIRD_ROW_INTERVAL
import com.example.investorssquare.util.ResourceMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Board(
    screenWidthDp: Dp,
    sideMargin: Dp,
) {
    val showPopup by showPopup.collectAsState()
    val estates by estates.collectAsState()
    val currentField by currentField.collectAsState()
    val showDebtPopup by BankruptcyService.debtPopupVisible.collectAsState()
    val boardSize = (screenWidthDp.value - sideMargin.value * 2).dp
    val fieldHeight = (boardSize.value * RELATIVE_FIELD_HEIGHT).dp
    val fieldWidth = ((boardSize.value - 2 * fieldHeight.value) / FIELDS_PER_ROW).dp
    var centerOfTheBoard by remember { mutableStateOf(IntOffset.Zero) }
    val board = board.value!!
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
                val imageResourceId = ResourceMapper.getImageResource(board.name)

                if (imageResourceId != null) {
                    Image(
                        painter = painterResource(id = imageResourceId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                CornerFieldCard(fieldHeight, Modifier.align(Alignment.BottomEnd),0)
                RowFieldCards(fieldHeight, fieldWidth, Modifier.align(Alignment.BottomCenter),0, board, onFieldClick = { })

                CornerFieldCard(fieldHeight, Modifier.align(Alignment.BottomStart), 10)
                RowFieldCards(fieldHeight, fieldWidth, Modifier.align(Alignment.BottomStart), 10, board, onFieldClick = { }, 90f,
                    -with(LocalDensity.current) { fieldWidth.toPx() } * 4.5f - with(LocalDensity.current) { fieldHeight.toPx() } * 0.5f,
                    -with(LocalDensity.current) { fieldHeight.toPx() } * 2.5f)

                CornerFieldCard(fieldHeight, Modifier.align(Alignment.TopStart), 20)
                RowFieldCards(fieldHeight, fieldWidth, Modifier.align(Alignment.BottomCenter), 20, board, onFieldClick = { }, 180f,
                    translationY = -with(LocalDensity.current) { fieldWidth.toPx() } * 9.0f - with(LocalDensity.current){fieldHeight.toPx()} * 1f)

                CornerFieldCard(fieldHeight, Modifier.align(Alignment.TopEnd), 30)
                RowFieldCards(fieldHeight, fieldWidth, Modifier.align(Alignment.BottomCenter), 30, board, onFieldClick = { }, 270f,
                    with(LocalDensity.current){fieldHeight.toPx()} * 2.5f,
                    -with(LocalDensity.current) { fieldWidth.toPx() } * 4.5f - with(LocalDensity.current){fieldHeight.toPx()} * 0.5f)

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
                                horizontal = estate.estate.index < 10 || (estate.estate.index in THIRD_ROW_INTERVAL)
                            )
                        }
                    }
                }
            }
        }
        if (showDebtPopup) {
            DebtPopup(
                centerOfTheBoard = centerOfTheBoard,
                popupWidth = (0.6f * FIELDS_PER_ROW * fieldWidth.value).dp,
                popupHeight = (0.6f * FIELDS_PER_ROW * fieldWidth.value).dp,
            )
        }

        currentField?.let { field ->
            if (showPopup) {
                DisplayPopup(field, fieldWidth, centerOfTheBoard, coroutineScope)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun DisplayPopup(
    field: Field,
    fieldWidth: Dp,
    centerOfTheBoard: IntOffset,
    coroutineScope: CoroutineScope
) {
    val activePlayer = getActivePlayer()
    val buyButtonVisibility = activePlayer?.canBuyEstate(field) ?: false

    when (field.type) {
        FieldType.PROPERTY -> PropertyDetails(
            field = field,
            onDismissRequest = { dismissPopupForField() },
            popupWidth = (RELATIVE_POPUP_WIDTH * fieldWidth.value).dp,
            popupHeight = (RELATIVE_POPUP_HEIGHT * FIELDS_PER_ROW * fieldWidth.value).dp,
            centerOfTheBoard = centerOfTheBoard,
            buyButtonVisibility = buyButtonVisibility
        )
        FieldType.STATION -> StationDetails(
            field = field,
            onDismissRequest = { dismissPopupForField() },
            popupWidth = (RELATIVE_POPUP_WIDTH * fieldWidth.value).dp,
            popupHeight = (RELATIVE_POPUP_HEIGHT * FIELDS_PER_ROW * fieldWidth.value).dp,
            centerOfTheBoard = centerOfTheBoard,
            buyButtonVisibility = buyButtonVisibility
        )
        FieldType.UTILITY -> UtilityDetails(
            field = field,
            onDismissRequest = { dismissPopupForField() },
            popupWidth = (RELATIVE_POPUP_WIDTH * fieldWidth.value).dp,
            popupHeight = (RELATIVE_POPUP_HEIGHT * FIELDS_PER_ROW * fieldWidth.value).dp,
            centerOfTheBoard = centerOfTheBoard,
            buyButtonVisibility = buyButtonVisibility
        )
        FieldType.CHANCE, FieldType.COMMUNITY_CHEST -> CommunityCardPopup(
            isChance = field.type == FieldType.CHANCE,
            onDismissRequest = {
                dismissPopupForField()
                coroutineScope.launch { postEvent(Event.ON_COMMUNITY_CARD_CLOSED) }
            },
            centerOfTheBoard = centerOfTheBoard,
            popupWidth = (RELATIVE_COMMUNITY_CARD_WIDTH * FIELDS_PER_ROW * fieldWidth.value).dp,
            popupHeight = (RELATIVE_COMMUNITY_CARD_HEIGHT * fieldWidth.value).dp,
        )
        FieldType.JAIL-> JailPopup(
            getActivePlayer()?.numberOfGetOutOfJailFreeCards!!.value>0,
            if(RuleBook.payToEscapeJailEnabled && getActivePlayer()!!.money.value >= RuleBook.jailEscapePrice)
                RuleBook.jailEscapePrice else -1,
            centerOfTheBoard = centerOfTheBoard,
            popupWidth = (0.8f * FIELDS_PER_ROW * fieldWidth.value).dp,
            popupHeight = (0.8f * FIELDS_PER_ROW * fieldWidth.value).dp,
        )
        else -> {}
    }
}