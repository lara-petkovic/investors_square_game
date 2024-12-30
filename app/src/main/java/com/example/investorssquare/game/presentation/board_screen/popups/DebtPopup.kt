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
import com.example.investorssquare.game.service.CommunityCardService
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
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
                Text("You can't finish a move while you're in debt.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = Color.Black
                )
                Button(
                    onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_REPAY_DEBT)}},
                    modifier = Modifier.height(30.dp).padding(0.dp)
                ) {
                    Text("Repay debt",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = Color.Black)
                }
                Button(
                    onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_DECLARE_BANKRUPTCY)}},
                    modifier = Modifier.height(30.dp).padding(0.dp)
                ) {
                    Text(
                        text = "Declare bankruptcy",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
