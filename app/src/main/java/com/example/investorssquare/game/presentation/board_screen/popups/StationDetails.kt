package com.example.investorssquare.game.presentation.board_screen.popups

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.investorssquare.R
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Station

@Composable
fun StationDetails(
    field: Field,
    onDismissRequest: () -> Unit,
    offset : IntOffset,
    popupWidth: Dp,
    popupHeight: Dp,
    board: Board
) {
    val station = field as? Station
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
                if (!station?.imageUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(context.resources.getIdentifier(station?.imageUrl?:"", "drawable", context.packageName)),
                        contentDescription = null,
                        modifier = Modifier
                            .height((popupHeight.value*0.30).dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = "${station?.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical=4.dp)){
                    Column(
                        modifier = Modifier.fillMaxWidth().height((popupHeight.value*0.48).dp).verticalScroll(scrollState).border(1.dp, Color.Black),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        for(i in 0..((station?.rent?.size?.minus(1)) ?: 0)){
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${i+1} ${if(i>0)station?.commonNamePlural else station?.commonName} owned",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 11.sp,
                                    color = Color.Black
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = "${station?.rent?.get(i) ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 11.sp,
                                        color = Color.Black
                                    )
                                    Box(modifier = Modifier.size(13.dp)) {
                                        Image(
                                            painter = painterResource(R.drawable.coin),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Mortgage Value ${station?.mortgagePrice ?: 0}",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                            Box(modifier = Modifier.size(13.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.coin),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Sell Value ${station?.sellPrice ?: 0}",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                            Box(modifier = Modifier.size(13.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.coin),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
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