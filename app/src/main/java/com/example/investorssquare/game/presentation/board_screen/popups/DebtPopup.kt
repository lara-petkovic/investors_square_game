package com.example.investorssquare.game.presentation.board_screen.popups


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.util.Constants.BUTTON_BORDER_WIDTH
import com.example.investorssquare.util.Constants.JAIL_POPUP_BUTTON_TEXT_SIZE
import kotlinx.coroutines.launch

@Composable
fun DebtPopup(
    centerOfTheBoard: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
) {
    val density = LocalDensity.current
    val popupWidthPx = with(density) { popupWidth.toPx() }
    val popupHeightPx = with(density) { popupHeight.toPx() }
    val coroutineScope = rememberCoroutineScope()

    val offsetX = centerOfTheBoard.x - (popupWidthPx / 2).toInt()
    val offsetY = centerOfTheBoard.y - (popupHeightPx / 2).toInt()

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
                    color = MaterialTheme.colorScheme.onTertiary,
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
                    text = "You can't finish a move while you're in debt.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(horizontal = 6.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch { EventBus.postEvent(Event.ON_REPAY_DEBT) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(BUTTON_BORDER_WIDTH.dp, color = MaterialTheme.colorScheme.onTertiary)
                    ) {
                        Text(
                            text = "Repay debt",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = JAIL_POPUP_BUTTON_TEXT_SIZE.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch { EventBus.postEvent(Event.ON_DECLARE_BANKRUPTCY) }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(BUTTON_BORDER_WIDTH.dp, color = MaterialTheme.colorScheme.onTertiary)
                    ) {
                        Text(
                            text = "Declare Bankruptcy",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = JAIL_POPUP_BUTTON_TEXT_SIZE.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }
        }
    }
}