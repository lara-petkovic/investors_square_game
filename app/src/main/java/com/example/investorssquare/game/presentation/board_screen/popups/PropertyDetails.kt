package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.buttons.BuyButton
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import com.example.investorssquare.game.presentation.board_screen.components.HouseIcon
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.util.ResourceMapper
import kotlinx.coroutines.launch

private val row_height = 20.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PropertyDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    popupWidth: Dp,
    popupHeight: Dp,
    centerOfTheBoard: IntOffset,  // Center of the board in pixel coordinates
    buyButtonVisibility: Boolean
) {
    val property = field as? Property ?: return
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints {
        // Convert popup dimensions from Dp to Px
        val density = LocalDensity.current
        val popupWidthPx = with(density) { popupWidth.toPx() }
        val popupHeightPx = with(density) { popupHeight.toPx() }

        // Calculate the popup's offset to center it on the board
        val offsetX = centerOfTheBoard.x - (popupWidthPx / 2).toInt()
        val offsetY = centerOfTheBoard.y - (popupHeightPx / 2).toInt()

        // Use the calculated offsets to position the popup
        Popup(
            onDismissRequest = { onDismissRequest() },
            properties = PopupProperties(focusable = true),
            offset = IntOffset(offsetX, offsetY)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PropertyHeader(property)
                        PropertyDetailsContent(property, scrollState)
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .padding(horizontal = 6.dp, vertical = 5.dp)
            .border(1.dp, Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PropertyRentRow(property.rent[0])
        for (i in 1 until (property.rent.size - 1)) {
            RentWithHousesRow(i, property.rent[i])
        }
        RentWithHotelRow(property.rent.last())
        Spacer(modifier = Modifier.weight(1f))
        PropertyCostRows(property)
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun RentWithHousesRow(index: Int, rent: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(row_height),
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
            HouseIcon(Modifier.size(16.dp))
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun RentWithHotelRow(rent: Int) {
    val boardName = Game.board.value?.name ?: "default"
    val hotelImageResource = ResourceMapper.getImageResource("${boardName}_hotel")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(row_height),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (hotelImageResource != null) {
            Image(
                painter = painterResource(id = hotelImageResource),
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

@Composable
private fun PropertyCostRows(property: Property) {
    Column(modifier=Modifier.padding(vertical = 5.dp, horizontal = 5.dp)
        .border(1.dp, Color.LightGray)
        .fillMaxWidth()
    ){
        Column(Modifier.padding(vertical = 3.dp)){
            PropertyCostRow("Building Cost", property.housePrice)
            PropertyCostRow("Mortgage Value", property.mortgagePrice)
            PropertyCostRow("Sell Value", property.sellPrice)
        }
    }
}

@Composable
private fun PropertyCostRow(label: String, cost: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.padding(vertical = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically,
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