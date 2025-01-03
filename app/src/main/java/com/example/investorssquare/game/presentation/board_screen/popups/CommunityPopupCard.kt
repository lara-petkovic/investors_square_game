package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.investorssquare.game.service.BoardService.board
import com.example.investorssquare.game.service.CommunityCardService

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CommunityCardPopup(
    isChance: Boolean,
    onDismissRequest: () -> Unit,
    centerOfTheBoard: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
) {
    var isFlipped by remember { mutableStateOf(false) }
    var isClosing by remember { mutableStateOf(false) }

    val card = remember { CommunityCardService.drawCard(isChance) }

    val density = LocalDensity.current
    val popupWidthPx = with(density) { popupWidth.toPx() }
    val popupHeightPx = with(density) { popupHeight.toPx() }

    val offsetX = centerOfTheBoard.x - (popupWidthPx / 2).toInt()
    val offsetY = centerOfTheBoard.y - (popupHeightPx / 2).toInt()

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 1400),
        label = ""
    )

    LaunchedEffect(Unit) {
        delay(300)
        isFlipped = true
        delay(calculateShowDuration(card.text))
        isFlipped = false
        isClosing = true
        delay(900)
        onDismissRequest()
    }
    Popup(
        onDismissRequest = { },
        properties = PopupProperties(focusable = true),
        offset = IntOffset(offsetX, offsetY)
    ) {

        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density.density
                }
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 3.dp,
                    color = (if (isChance) board.value?.chance?.primaryColor
                            else board.value?.communityChest?.primaryColor)!!,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(
                    color = (if (isChance)
                        board.value?.chance?.primaryColor?.copy(alpha = 0.1f)
                    else
                        board.value?.communityChest?.primaryColor?.copy(alpha = 0.1f))!!
                )
        ) {
            if (rotation <= 90f) {
                Image(
                    painter = painterResource(id = if(isChance) R.drawable.chance_card else R.drawable.community_chest_card),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp).graphicsLayer {
                        scaleX = -1f
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = (if (isChance) board.value?.chance?.commonName?.uppercase()
                                else board.value?.communityChest?.commonName?.uppercase())!!,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        color = (if (isChance) board.value?.chance?.primaryColor
                                else board.value?.communityChest?.primaryColor)!!,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier.fillMaxSize().border(1.dp, Color.Black).background(color = Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            text = card.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

fun calculateShowDuration(text: String): Long {
    return (maxOf(text.length*40, 2000)).toLong()
}