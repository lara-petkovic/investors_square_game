package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.R
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.Constants.BUY
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "DiscouragedApi")
@Composable
fun PropertyDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    offset: IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    buyButtonVisibility: Boolean
) {
    val property = field as? Property ?: return
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Popup(
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(focusable = true),
        offset = offset
    ) {
        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            if (property.imageUrl.isNotEmpty()) {
                Image(
                    painter = painterResource(context.resources.getIdentifier(property.imageUrl, "drawable", context.packageName)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .graphicsLayer(alpha = 0.2f),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PropertyHeader(property)
                PropertyDetailsContent(property, scrollState, popupHeight)
                if (buyButtonVisibility) {
                    BuyButton(popupWidth, popupHeight) {
                        GlobalScope.launch { EventBus.postEvent(Event.ON_BUYING_ESTATE(field.index)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyHeader(property: Property) {
    val backgroundColor = property.setColor
    val textColor = if (backgroundColor.luminance() > 0.4f) Color.Black else Color.White

    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(42.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = property.name,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun PropertyRentRow(rent: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "RENT $rent",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = Color.Black
        )
        CoinIcon(size = 16.dp)
    }
}

@Composable
private fun PropertyDetailsContent(
    property: Property,
    scrollState: androidx.compose.foundation.ScrollState,
    popupHeight: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height((popupHeight.value * 0.72).dp)
            .verticalScroll(scrollState)
            .padding(4.dp)
            .border(1.dp, Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PropertyRentRow(property.rent[0])
        for (i in 1 until (property.rent.size - 1)) {
            RentWithHousesRow(i, property.rent[i])
        }
        RentWithHotelRow(property.rent.last())
        PropertyCostRows(property)
    }
}

@Composable
private fun RentWithHousesRow(index: Int, rent: Int) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(30.dp), // Fixed height for the row
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index × ",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.Black
            )
            Image(
                painter = painterResource(
                    context.resources.getIdentifier(
                        Game.board.value?.houseImageUrl, "drawable", context.packageName
                    )
                ),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
        DotSeparator(rent)
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rent.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.Black
            )
            CoinIcon(size = 16.dp)
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
private fun RentWithHotelRow(rent: Int) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(30.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(
                context.resources.getIdentifier(
                    Game.board.value?.hotelImageUrl, "drawable", context.packageName
                )
            ),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        DotSeparator(rent)
        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rent.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color.Black
            )
            CoinIcon(size = 16.dp)
        }
    }
}

@Composable
private fun PropertyCostRows(property: Property) {
    PropertyCostRow("Building Cost", property.housePrice)
    PropertyCostRow("Mortgage Value", property.mortgagePrice)
    PropertyCostRow("Sell Value", property.sellPrice)
}

@Composable
private fun PropertyCostRow(label: String, cost: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label $cost",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            color = Color.Black
        )
        CoinIcon(size = 14.dp)
    }
}

@Composable
private fun CoinIcon(size: Dp) {
    Image(
        painter = painterResource(R.drawable.coin),
        contentDescription = null,
        modifier = Modifier.size(size)
    )
}

@Composable
private fun DotSeparator(price: Int) {
    var dots by remember { mutableStateOf("...") }
    val density = LocalDensity.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dots,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            onTextLayout = { textLayoutResult ->
                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                dots = ".".repeat(dotCount)
            }
        )
        Text(
            text = price.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = Color.Black
        )
        CoinIcon(size = 16.dp)
    }
}

@Composable
private fun BuyButton(popupWidth: Dp, popupHeight: Dp, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(2.dp))
    Button(
        onClick = onClick,
        modifier = Modifier
            .size((popupWidth.value * 0.5).dp, (popupHeight.value * 0.07).dp)
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(BUY, color = Color.White, style = MaterialTheme.typography.labelSmall)
    }
    Spacer(modifier = Modifier.height(3.dp))
}