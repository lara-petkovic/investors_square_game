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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Board
import kotlinx.coroutines.delay
import com.example.investorssquare.R
import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CommunityCardPopup(
    isChance: Boolean,
    onDismissRequest: () -> Unit,
    offset : IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    boardViewModel: BoardViewModel
) {
    var isFlipped by remember { mutableStateOf(false) }
    var isClosing by remember { mutableStateOf(false) }

    val card = remember {
        if (isChance) boardViewModel.board.value?.chance?.drawCard() else boardViewModel.board.value?.communityChest?.drawCard()
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 1400)
    )

    LaunchedEffect(Unit) {
        delay(300)
        isFlipped = true
        delay(4000)
        isFlipped = false
        isClosing = true
        delay(900)
        onDismissRequest()
    }
    Popup(
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(focusable = true),
        offset = offset
    ) {

        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 3.dp,
                    color = (if (isChance) boardViewModel.board.value?.chance?.primaryColor else boardViewModel.board.value?.communityChest?.primaryColor)!!,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(
                    color = (if (isChance)
                        boardViewModel.board.value?.chance?.primaryColor?.copy(alpha = 0.1f)
                    else
                        boardViewModel.board.value?.communityChest?.primaryColor?.copy(alpha = 0.1f))!!
                )
        ) {
            if (rotation <= 90f) {
                Image(
                    painter = painterResource(id = if(isChance) R.drawable.chance_card else R.drawable.community_chest_card),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp).graphicsLayer {
                        scaleX = -1f
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = (if (isChance) boardViewModel.board.value?.chance?.commonName?.uppercase() else boardViewModel.board.value?.communityChest?.commonName?.uppercase())!!,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        color = (if (isChance) boardViewModel.board.value?.chance?.primaryColor else boardViewModel.board.value?.communityChest?.primaryColor)!!,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier.fillMaxSize().border(1.dp, Color.Black).background(color=Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (card != null) {
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
}
