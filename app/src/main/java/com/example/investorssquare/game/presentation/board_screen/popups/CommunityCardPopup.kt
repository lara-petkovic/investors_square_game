package com.example.investorssquare.game.presentation.board_screen.popups

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Board

@Composable
fun CommunityCardPopup(
    isChance: Boolean,
    onDismissRequest: () -> Unit,
    offset : IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    board: Board
) {
    val context = LocalContext.current
    Popup(
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(focusable = true),
        offset = offset
    ) {
        val card = if(isChance) board.drawChanceCard() else board.drawCommunityChestCard()
        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(5.dp))
                .border(
                    width = 4.dp,
                    color = if(isChance) board.chancePrimaryColor else board.communityChestPrimaryColor,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = if(isChance) board.chanceCommonName.uppercase() else board.communityChestCommonName.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    color = if(isChance) board.chancePrimaryColor else board.communityChestPrimaryColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier= Modifier.height(5.dp))
                Text(
                    text = card.text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
