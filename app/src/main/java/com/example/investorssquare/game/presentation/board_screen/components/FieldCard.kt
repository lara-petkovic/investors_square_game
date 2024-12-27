package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.FieldType
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.BuildingService
import com.example.investorssquare.game.service.SellingService
import com.example.investorssquare.util.Constants.FIELD_CARD_STRAP_HEIGHT_PERCENTAGE
import com.example.investorssquare.util.ResourceMapper
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun FieldCard(
    fieldWidth: Dp,
    fieldHeight: Dp,
    field: Field,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    //ove tri stari sam dodao da kad se upali mod za gradnju mogu da prepoznam gde mogu a gde ne mogu da gradim
    val buildingModeOn = BuildingService.buildingModeOn.collectAsState()
    val sellingModeOn = SellingService.sellingModeOn.collectAsState()
    val estate = Game.getEstateByFieldIndex(field.index)
    val openToBuild = if(estate!= null && estate.isProperty) estate.isOpenToBuild.collectAsState() else mutableStateOf(false)
    val openToSell = if(estate!= null && estate.isProperty) estate.isOpenToSell.collectAsState() else mutableStateOf(false)

    Box(
        modifier = modifier
            .clickable { coroutineScope.launch { EventBus.postEvent(Event.ON_FIELD_CLICKED(field.index)) } }
            .background(
                // ovo popraviti da bude jednako zatamnjeno na svakom polju (ne znam zasto ne radi)
                // svakako treba izrefaktorisati ja se nisam puno bavio UIem pa sam stavio na prvo logicno mesto
                if(buildingModeOn.value && !openToBuild.value)
                    Color(0f, 0f, 0f, 0.3025f)
                else if(sellingModeOn.value && !openToSell.value)
                    Color(0f, 0f, 0f, 0.3025f)
                else Color.Transparent
            )
    ) {
        Card(
            modifier = Modifier.size(fieldWidth, fieldHeight),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            FieldCardContent(field = field, fieldWidth = fieldWidth, fieldHeight = fieldHeight)
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun FieldCardContent(
    field: Field,
    fieldWidth: Dp,
    fieldHeight: Dp
) {
    val isFieldOwned = Game.getEstateByFieldIndex(field.index)?.ownerIndex?.value != -1

    Box(modifier = Modifier.fillMaxSize()) {
        OwnershipIndicator(isOwned = isFieldOwned, field)

        if (field.type == FieldType.PROPERTY) {
            PropertyStrap(
                property = field as? Property,
                fieldHeight = fieldHeight,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }

        PlayerDrawer(
            canvasHeight = fieldHeight * (1 + FIELD_CARD_STRAP_HEIGHT_PERCENTAGE),
            canvasWidth = fieldWidth,
            fieldIndex = field.index
        )
    }
}

@Composable
private fun PropertyStrap(
    property: Property?,
    fieldHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(fieldHeight * FIELD_CARD_STRAP_HEIGHT_PERCENTAGE)
            .background(property?.setColor ?: Color.Gray)
            .drawBehind {
                val strokeWidth = 1.5.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Black,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        property?.let {
            val estateVM = Game.getEstateByFieldIndex(it.index)
            val numberOfHouses = estateVM?.numberOfBuildings?.collectAsState()?.value ?: 0

            if(property.rent.size - 1 == numberOfHouses) { // property size - 1 because there can be 0 houses
                DrawHotel(Modifier.align(Alignment.Center));
            }
            else {
                DrawHouses(
                    numberOfHouses = numberOfHouses,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun OwnershipIndicator(isOwned: Boolean, field: Field) {
    val buildingModeOn = BuildingService.buildingModeOn.collectAsState()
    val sellingModeOn = SellingService.sellingModeOn.collectAsState()
    var alpha = 0.0f
    if(Game.getEstateByFieldIndex(field.index)!=null && isOwned && !buildingModeOn.value && !sellingModeOn.value){
        alpha = 0.5f
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(Color(0xFFDAF7DB))
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DrawHotel(modifier: Modifier = Modifier) {
    val boardName = Game.board.value?.name ?: "default"
    val hotelImageResource = ResourceMapper.getImageResource("${boardName}_hotel")

    if (hotelImageResource != null) {
        Image(
            painter = painterResource(id = hotelImageResource),
            contentDescription = "hotel image",
            modifier = modifier.size(10.dp)
        )
    }
}

@Composable
fun DrawHouses(
    numberOfHouses: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (numberOfHouses) {
            in 0..3 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(numberOfHouses) {
                        HouseIcon(Modifier.size(8.dp))
                    }
                }
            }
            in 4..6 -> {
                var i = 0
                if (numberOfHouses % 2 != 0) {
                    i = 1
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(numberOfHouses / 2) { HouseIcon(Modifier.size(8.dp)) }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(numberOfHouses / 2 + i) { HouseIcon(Modifier.size(8.dp)) }
                    }
                }
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "x$numberOfHouses",
                        fontSize = 7.sp,
                        modifier = Modifier.offset(y = (-5).dp)
                    )
                    HouseIcon(Modifier.size(10.dp).padding(top = 3.dp))
                }
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HouseIcon(
    modifier: Modifier = Modifier
) {
    val boardName = Game.board.value?.name ?: "default"
    val houseImageResource = ResourceMapper.getImageResource("${boardName}_house")

    if (houseImageResource != null) {
        Image(
            painter = painterResource(id = houseImageResource),
            contentDescription = "house image",
            modifier = modifier
        )
    }
}