package com.example.investorssquare.game.presentation.board_screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.service.EstateService.getEstateByFieldIndex
import com.example.investorssquare.game.service.EstateService.getOwnerOfEstate
import com.example.investorssquare.util.Constants.TOTAL_FIELDS

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BoughtEstateMarker(
    fieldWidth: Dp,
    field: Field,
    modifier: Modifier,
    horizontal: Boolean = true
) {
    val estate = getEstateByFieldIndex(field.index)
    val ownerIndex = estate?.ownerIndex?.collectAsState()?.value?:-1

    val width = if (horizontal) fieldWidth / 2 else fieldWidth / 3.7f
    val height = if (horizontal) fieldWidth / 3.7f else fieldWidth / 2

    if(ownerIndex>-1){
        Box(
            modifier = modifier
                .background(getOwnerOfEstate(estate!!.estate.index)!!.color.value)
                .size(width, height)
                .border(1.dp, color = Color.Black)
        )
    }
}

fun calculateXOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return when {
        fieldIndex < 10 -> boardSize - (fieldHeight.value + (fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 20 -> boardSize - (fieldHeight.value + 8.5 * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 30 -> boardSize - (fieldHeight.value + (30 - fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        else -> boardSize - fieldHeight - (0.25 * fieldWidth.value).dp
    }
}

fun calculateYOffsetForBoughtEstateMarker(fieldIndex: Int, fieldHeight: Dp, fieldWidth: Dp, boardSize: Dp): Dp {
    return when {
        fieldIndex < 10 -> boardSize - fieldHeight - (0.25 * fieldWidth.value).dp
        fieldIndex < 20 -> boardSize - (fieldHeight.value + (fieldIndex - 11 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        fieldIndex < 30 -> boardSize - (fieldHeight.value + 8.5 * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
        else -> boardSize - (fieldHeight.value + (TOTAL_FIELDS - fieldIndex - 1 + 0.25) * fieldWidth.value).dp - (0.5 * fieldWidth.value).dp
    }
}