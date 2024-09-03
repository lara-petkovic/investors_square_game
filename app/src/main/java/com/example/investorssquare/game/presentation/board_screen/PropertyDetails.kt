package com.example.investorssquare.game.presentation.board_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // This import covers all layout functions like size, height, and align
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // Correct import for Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.domain.model.Property

@Composable
fun PropertyDetailsPopup(
    field: Field,
    onDismissRequest: () -> Unit,
    offset : IntOffset,
    popupWidth: Dp,
    popupHeight: Dp
) {
    Popup(
        onDismissRequest = { onDismissRequest() },
        properties = PopupProperties(
            focusable = true
        ),
        offset = offset
    ) {
        Box(
            modifier = Modifier
                .size(popupWidth, popupHeight)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Field Details",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = field.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}
