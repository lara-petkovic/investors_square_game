package com.example.investorssquare.game.presentation.board_screen.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.investorssquare.R
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import kotlinx.coroutines.launch

@Composable
fun RedeemButton(
    isButtonClicked: Boolean,
    onButtonClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            onButtonClicked() // Update state in parent
            coroutineScope.launch { EventBus.postEvent(Event.ON_SWITCH_TO_REDEEM_MODE) }
        },
        modifier = Modifier
            .padding(top = 10.dp)
            .size(54.dp)
            .background(
                if (isButtonClicked) Color.Gray else Color.White,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = if (isButtonClicked) Color.White else Color.Black,
                shape = CircleShape
            )
    ) {
        val buildIcon: Painter = painterResource(id = R.drawable.icon_redeem)
        Image(
            painter = buildIcon,
            contentDescription = "Build",
            modifier = Modifier.size(49.dp),
            colorFilter = if (isButtonClicked) ColorFilter.tint(Color.White) else ColorFilter.tint(
                Color.Black
            )
        )
    }
}
