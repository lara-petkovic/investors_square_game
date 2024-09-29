package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.investorssquare.game.domain.model.Station
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.BuyButton
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import kotlinx.coroutines.launch

@Composable
fun StationDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    offset: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    buyButtonVisibility: Boolean
) {
    val station = field as? Station ?: return
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Popup(
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
        offset = offset
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(popupWidth, popupHeight)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StationImage(station.imageUrl, context, popupHeight)
                    StationInfo(station)
                    StationDetailsContent(station, scrollState)
                }
            }
            if (buyButtonVisibility) {
                Spacer(modifier = Modifier.width(16.dp))

                BuyButton(popupWidth, popupHeight) {
                    coroutineScope.launch {
                        EventBus.postEvent(Event.ON_ESTATE_BOUGHT(field.index))
                    }
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun StationImage(imageUrl: String, context: Context, popupHeight: Dp) {
    Image(
        painter = painterResource(context.resources.getIdentifier(imageUrl, "drawable", context.packageName)),
        contentDescription = null,
        modifier = Modifier.height((popupHeight.value * 0.3).dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun StationInfo(station: Station) {
    Text(
        text = station.name,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 14.sp,
        color = Color.Black,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun StationDetailsContent(station: Station, scrollState: androidx.compose.foundation.ScrollState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .border(1.dp, Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            station.rent.forEachIndexed { index, rent ->
                RentRow(
                    station = station,
                    rent = rent,
                    index = index
                )
            }
            StationCostRow(label = "Mortgage Value", cost = station.mortgagePrice)
            StationCostRow(label = "Sell Value", cost = station.sellPrice)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun RentRow(station: Station, rent: Int, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${index + 1} ${if (index > 0) station.commonNamePlural else station.commonName} owned",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 11.sp,
            color = Color.Black
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rent",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                color = Color.Black
            )
            CoinIcon(13.dp)
        }
    }
}

@Composable
fun StationCostRow(label: String, cost: Int) {
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
        CoinIcon(13.dp)
    }
}