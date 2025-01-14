package com.example.investorssquare.game.presentation.board_screen.popups

import android.annotation.SuppressLint
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.components.CoinIcon
import com.example.investorssquare.game.presentation.board_screen.components.buttons.BuyButton
import com.example.investorssquare.game.service.EstateService.getPropertyByFieldIndex
import com.example.investorssquare.game.service.EstateService.getStationByFieldIndex
import com.example.investorssquare.game.service.EstateService.getUtilityByFieldIndex
import com.example.investorssquare.game.service.TradeService.closeTradeWindow
import com.example.investorssquare.game.service.TradeService.counterTradeOffer
import com.example.investorssquare.game.service.TradeService.creatingTradeOffer
import com.example.investorssquare.game.service.TradeService.initiatorPlayer
import com.example.investorssquare.game.service.TradeService.initiatorPlayersEstates
import com.example.investorssquare.game.service.TradeService.moneyToReceive
import com.example.investorssquare.game.service.TradeService.moneyToSend
import com.example.investorssquare.game.service.TradeService.reviewingTradeOffer
import com.example.investorssquare.game.service.TradeService.setMoney
import com.example.investorssquare.game.service.TradeService.targetedPlayer
import com.example.investorssquare.game.service.TradeService.targetedPlayersEstates
import kotlinx.coroutines.launch

@Composable
fun TradePopup(
    modifier:Modifier,
) {
    val creatingTradeOffer = creatingTradeOffer.collectAsState()
    val reviewingTradeOffer = reviewingTradeOffer.collectAsState()
    val counterTradeOffer = counterTradeOffer.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier
        .background(Color.White, shape = RoundedCornerShape(8.dp))
        .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        Popup(
            onDismissRequest = { closeTradeWindow() },
            properties = PopupProperties(focusable = true),
        ) {
            Column(modifier = modifier){
                Box(modifier=Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(255,228,10), shape = RoundedCornerShape(8.dp))
                ){
                    Text(modifier = Modifier.align(Alignment.Center), fontSize = 18.sp, text="Trade offer")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        TradeColumn(true)
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Gray)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        TradeColumn(false)
                    }
                }
                Box(modifier=Modifier.padding(8.dp)){
                    Box(modifier=Modifier
                        .border(1.dp, Color.Black)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(80.dp)
                    )
                }
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    if(creatingTradeOffer.value || counterTradeOffer.value)
                        Button(onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_CREATING_A_TRADE) }}){ Text("Offer a trade") }
                    if(reviewingTradeOffer.value){
                        Button(onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_ACCEPTING_A_TRADE) }}){ Text("Accept") }
                        Button(onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_DECLINING_A_TRADE) }}){ Text("Decline") }
                    }
                }
            }
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun TradeColumn(left:Boolean){
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val creatingTradeOffer = creatingTradeOffer.collectAsState()
    val reviewingTradeOffer = reviewingTradeOffer.collectAsState()
    val counterTradeOffer = counterTradeOffer.collectAsState()
    val player = if(left) initiatorPlayer.collectAsState() else targetedPlayer.collectAsState()
    val estates = if(left) initiatorPlayersEstates.collectAsState() else targetedPlayersEstates.collectAsState()
    val money = if(left) moneyToSend.collectAsState() else moneyToReceive.collectAsState()
    var sliderPosition by remember { mutableFloatStateOf(money.value.toFloat()) }
    LaunchedEffect(money.value) {
        sliderPosition = money.value.toFloat()
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        if(!left && !reviewingTradeOffer.value) Button(onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_CHANGE_TARGET_PLAYER(false)) }}, modifier = Modifier.height(22.dp)) { Text("p") }
        Text(player.value!!.name.value)
        if(!left && !reviewingTradeOffer.value) Button(onClick = {coroutineScope.launch { EventBus.postEvent(Event.ON_CHANGE_TARGET_PLAYER(true)) }}, modifier = Modifier.height(22.dp)) { Text("n") }
    }
    Box(Modifier.height(1.dp).fillMaxWidth().background(Color.Gray).padding(vertical = 2.dp))
    Text(text = "Choose properties to "+ if(left) "send" else "receive", fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Box(Modifier.height(300.dp).fillMaxWidth()){
        Column(modifier = Modifier.verticalScroll(state = scrollState).align(Alignment.Center)){
            for(e in estates.value){
                if(e.isProperty){
                    PropertyCard(160.dp, 180.dp, getPropertyByFieldIndex(e.estate.index), left)
                }
                else if(e.isStation){
                    StationCard(160.dp, 180.dp, getStationByFieldIndex(e.estate.index), left)
                }
                else if(e.isUtility){
                    UtilityCard(160.dp, 180.dp, getUtilityByFieldIndex(e.estate.index), left)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    Box(Modifier.height(1.dp).fillMaxWidth().background(Color.Gray).padding(vertical = 2.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Text("Money to "+ (if(left)"send: " else "receive: ") + money.value, fontSize=13.sp)
            CoinIcon(15.dp, modifier=Modifier.padding(top = 3.dp))
        }
        Slider(
            value = sliderPosition,
            onValueChange = { newValue -> sliderPosition = newValue; setMoney(newValue.toInt(), left) },
            valueRange = 0f..player.value!!.money.value.toFloat(),
            steps = 100,
            enabled = !reviewingTradeOffer.value,
            modifier = Modifier.height(20.dp).padding(vertical = 2.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Green,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray,
                activeTickColor = Color.Green,
                inactiveTickColor = Color.Gray,
                disabledThumbColor = Color.Green,
                disabledActiveTrackColor = Color.Green,
                disabledActiveTickColor = Color.Green,
                disabledInactiveTickColor = Color.Gray,
                disabledInactiveTrackColor = Color.Gray
            )
        )
    }
    //Box(Modifier.height(1.dp).fillMaxWidth().background(Color.Gray).padding(top = 50.dp))
}