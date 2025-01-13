package com.example.investorssquare.game.presentation.board_screen.popups


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.R
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.BoardService.dismissPopupForField
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun JailPopupPreview() {
    JailPopup(
        getOutOfJailFree = true,
        bailOutPrice = 100,
        centerOfTheBoard = IntOffset(325, 325),
        popupWidth = 250.dp,
        popupHeight = 250.dp,
        mockJailSentence = 2
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun JailPopup(
    getOutOfJailFree: Boolean,
    bailOutPrice: Int,
    centerOfTheBoard: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    mockJailSentence: Int? = null
) {
    val density = LocalDensity.current
    val popupWidthPx = with(density) { popupWidth.toPx() }
    val popupHeightPx = with(density) { popupHeight.toPx() }
    val coroutineScope = rememberCoroutineScope()

    val offsetX = centerOfTheBoard.x - (popupWidthPx / 2).toInt()
    val offsetY = centerOfTheBoard.y - (popupHeightPx / 2).toInt()

    val jailSentence = mockJailSentence ?: getActivePlayer()?.jailSentence?.value ?: 3

    Popup(
        properties = PopupProperties(focusable = true),
        offset = IntOffset(offsetX, offsetY)
    ) {
        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 3.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(10.dp)
            ) {
                Text(
                    text = buildString {
                        append("You are in Jail and sentenced to wait $jailSentence more ")
                        append(if (jailSentence == 1) "move" else "moves")
                        append(". You can choose an option:")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Justify
                )

                HorizontalDivider(modifier = Modifier.width((popupWidth.value * 0.7).dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (getOutOfJailFree) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        EventBus.postEvent(Event.ON_USE_GET_OUT_OF_JAIL_FREE_CARD)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color.Gray),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.cards),
                                        contentDescription = "Get Out of Jail Free Icon",
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Use card",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                        }

                        if (RuleBook.rollADoubleToEscapeJailEnabled) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        EventBus.postEvent(Event.ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color.Gray),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.double_dice),
                                        contentDescription = "Roll a Double Dice Icon",
                                        modifier = Modifier.size(25.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Roll double",
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 12.sp),
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (bailOutPrice > 0) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        EventBus.postEvent(Event.ON_BAIL_OUT)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, Color.Gray),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.dollar_sign),
                                        contentDescription = "Dismiss Icon",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Pay $bailOutPrice",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(1.dp))
                                    CoinIcon(size = 13.dp)
                                }
                            }

                        }

                        Button(
                            onClick = { dismissPopupForField() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.dismiss_turn),
                                    contentDescription = "Dismiss Icon",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Wait",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }

                    }
                }

            }
        }
    }
}