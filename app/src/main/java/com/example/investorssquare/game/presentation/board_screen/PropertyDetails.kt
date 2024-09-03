package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Property

@Composable
fun PropertyDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    offset : IntOffset,
    popupWidth: Dp,
    popupHeight: Dp
) {
    val property = field as? Property
    val scrollState = rememberScrollState()
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
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(property?.setColor ?: Color.Gray)
                        .fillMaxWidth()
                        .height((0.15*popupHeight.value).dp),
                    contentAlignment = Alignment.Center
                ){
                    val backgroundColor = property?.setColor ?: Color.Gray
                    val textColor = if (backgroundColor.luminance() > 0.4f) Color.Black else Color.White
                    Text(
                        text = property?.name ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().height((popupHeight.value*0.72).dp).verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "RENT ${property?.rent?.get(0) ?: 0}\uD83E\uDE99",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Column {
                        for(i in 1..((property?.rent?.size?.minus(2)) ?: 0)){
                            var dots by remember { mutableStateOf("...") }
                            val density = LocalDensity.current
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$i × \uD83C\uDFE0",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = dots,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    onTextLayout = { textLayoutResult ->
                                        val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                        val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                        val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                        dots = ".".repeat(dotCount)
                                    }
                                )
                                Text(
                                    text = "${property?.rent?.get(i) ?: 0}\uD83E\uDE99",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                        }
                        var dots by remember { mutableStateOf("...") }
                        val density = LocalDensity.current
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "\uD83C\uDFE8",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Green
                            )
                            Text(
                                text = dots,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                onTextLayout = { textLayoutResult ->
                                    val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                    val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                    val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                    dots = ".".repeat(dotCount)
                                }
                            )
                            Text(
                                text = "${property?.rent?.get(property?.rent?.size?.minus(1)?:0) ?: 0}\uD83E\uDE99",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Building Cost ${property?.housePrice ?: 0}\uD83E\uDE99",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Text(
                        text = "Mortgage Value ${property?.mortgagePrice ?: 0}\uD83E\uDE99",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Text(
                        text = "Sell Value ${property?.sellPrice ?: 0}\uD83E\uDE99",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .size((popupWidth.value * 0.5).dp, (popupHeight.value * 0.07).dp)
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("BUY", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}
