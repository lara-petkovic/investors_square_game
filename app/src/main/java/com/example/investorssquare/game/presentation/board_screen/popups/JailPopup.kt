package com.example.investorssquare.game.presentation.board_screen.popups


import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import com.example.investorssquare.R
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.CommunityCardService
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun JailPopup(
    getOutOfJailFree: Boolean,
    bailOutPrice: Int,
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
                .background(Color.White, shape = RoundedCornerShape(8.dp))
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
                modifier = Modifier.fillMaxSize().align(Alignment.Center).padding(10.dp)
            ){
                Text("You have to wait "+Game.getActivePlayer()?.jailSentence!!.value+" more " +
                    if(Game.getActivePlayer()?.jailSentence!!.value==1) "move" else "moves",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = Color.Black
                )
                if(getOutOfJailFree){
                    Button(
                        onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_USE_GET_OUT_OF_JAIL_FREE_CARD)}},
                        modifier = Modifier.height(30.dp).padding(0.dp)
                    ) {
                        Text("Use get out of jail free card",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = Color.Black)
                    }
                }
                if(bailOutPrice>0){
                    Button(
                        onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_BAIL_OUT)}},
                        modifier = Modifier.height(30.dp).padding(0.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pay bail out ($bailOutPrice",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            CoinIcon(size = 13.dp)
                            Text(
                                text = ")",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
                if(Game.ruleBook.rollADoubleToEscapeJailEnabled){
                    Button(
                        onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL)}},
                        modifier = Modifier.height(30.dp).padding(0.dp)
                    ) {
                        Text("Roll a double to escape",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = Color.Black)
                    }
                }
                Button(onClick = {Game.dismissPopup()}, modifier = Modifier.height(30.dp).padding(0.dp)) {
                    Text("Dismiss",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = Color.Black)
                }
            }
        }
    }
}