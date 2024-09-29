package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Utility
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.BuyButton
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import kotlinx.coroutines.launch

@Composable
fun UtilityDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    offset: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    buyButtonVisibility: Boolean
) {
    val utility = field as? Utility ?: return
    val coroutineScope = rememberCoroutineScope()

    Popup(
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(focusable = true),
        offset = offset
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            UtilityPopupContent(
                utility = utility,
                popupWidth = popupWidth,
                popupHeight = popupHeight
            )

            if (buyButtonVisibility) {
                Spacer(modifier = Modifier.width(16.dp))
                BuyButton(popupWidth, popupHeight) {
                    coroutineScope.launch {
                        EventBus.postEvent(Event.ON_BUYING_ESTATE(field.index))
                    }
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun UtilityPopupContent(
    utility: Utility,
    popupWidth: Dp,
    popupHeight: Dp
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(popupWidth, popupHeight)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UtilityHeader(utility)
            Spacer(modifier = Modifier.height(8.dp))

            if (utility.imageUrl.isNotEmpty()) {
                Image(
                    painter = painterResource(context.resources.getIdentifier(utility.imageUrl, "drawable", context.packageName)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(popupHeight * 0.26f),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            UtilityDetailsContent(utility, scrollState)
        }
    }
}

@Composable
private fun UtilityHeader(utility: Utility) {
    Text(
        text = utility.name,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 14.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(8.dp)
    )
}

@Composable
private fun UtilityDetailsContent(
    utility: Utility,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .border(1.dp, Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UtilityRentRows(utility)
        UtilityCostRows(utility)
    }
}

@Composable
private fun UtilityRentRows(utility: Utility) {
    for (i in 0 until utility.rent.size) {
        val rentDescription = "${i + 1} ${if (i > 0) utility.commonNamePlural else utility.commonName} owned - ${utility.rent[i]} times amount shown on dice"
        Text(
            text = rentDescription,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = Color.Black,
            modifier = Modifier.padding(3.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
    }
}

@Composable
private fun UtilityCostRows(utility: Utility) {
    UtilityCostRow("Mortgage Value", utility.mortgagePrice)
    UtilityCostRow("Sell Value", utility.sellPrice)
}

@Composable
private fun UtilityCostRow(label: String, cost: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label $cost",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = Color.Black
        )
        CoinIcon(size = 13.dp)
    }
}