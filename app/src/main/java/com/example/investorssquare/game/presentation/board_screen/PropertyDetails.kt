package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .background(property?.setColor ?: Color.Gray)
                        .fillMaxWidth()
                        .height((0.15*popupHeight.value).dp),
                    contentAlignment = Alignment.Center
                ){
                    val backgroundColor = property?.setColor ?: Color.Gray
                    val textColor = if (backgroundColor.luminance() > 0.3f) Color.Black else Color.White
                    Text(
                        text = property?.name ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "RENT ${property?.rent?.get(0) ?: 0}\uD83E\uDE99",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(3.dp))
                Column {
                    var dots1 by remember { mutableStateOf(".") }
                    var dots2 by remember { mutableStateOf(".") }
                    var dots3 by remember { mutableStateOf(".") }
                    var dots4 by remember { mutableStateOf(".") }
                    var dotsh by remember { mutableStateOf(".") }
                    val density = LocalDensity.current
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "\uD83C\uDFE0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                        Text(
                            text = dots1,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            onTextLayout = { textLayoutResult ->
                                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                dots1 = ".".repeat(dotCount)
                            }
                        )
                        Text(
                            text = "${property?.rent?.get(1) ?: 0}\uD83E\uDE99",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "\uD83C\uDFE0 \uD83C\uDFE0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                        Text(
                            text = dots2,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            onTextLayout = { textLayoutResult ->
                                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                dots2 = ".".repeat(dotCount)
                            }
                        )
                        Text(
                            text = "${property?.rent?.get(2) ?: 0}\uD83E\uDE99",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "\uD83C\uDFE0 \uD83C\uDFE0 \uD83C\uDFE0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                        Text(
                            text = dots3,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            onTextLayout = { textLayoutResult ->
                                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                dots3 = ".".repeat(dotCount)
                            }
                        )
                        Text(
                            text = "${property?.rent?.get(3) ?: 0}\uD83E\uDE99",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "\uD83C\uDFE0 \uD83C\uDFE0 \uD83C\uDFE0 \uD83C\uDFE0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Green
                        )
                        Text(
                            text = dots4,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            onTextLayout = { textLayoutResult ->
                                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                dots4 = ".".repeat(dotCount)
                            }
                        )
                        Text(
                            text = "${property?.rent?.get(4) ?: 0}\uD83E\uDE99",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
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
                            text = dotsh,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            onTextLayout = { textLayoutResult ->
                                val spaceWidth = with(density) { textLayoutResult.size.width.toDp() }
                                val dotWidth = with(density) { textLayoutResult.getBoundingBox(0).width.toDp() }
                                val dotCount = (spaceWidth / dotWidth).toInt().coerceAtLeast(0)
                                dotsh = ".".repeat(dotCount)
                            }
                        )
                        Text(
                            text = "${property?.rent?.get(5) ?: 0}\uD83E\uDE99",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Building Cost ${property?.housePrice ?: 0}\uD83E\uDE99",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Mortgage Value ${property?.mortgagePrice ?: 0}\uD83E\uDE99",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Sell Value ${property?.sellPrice ?: 0}\uD83E\uDE99",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
//                Button(
//                    onClick = { onDismissRequest() },
//                    modifier = Modifier.align(Alignment.End)
//                ) {
//                    Text("Close")
//                }
            }
        }
    }
}
