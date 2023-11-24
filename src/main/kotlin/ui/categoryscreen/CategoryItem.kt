package ui.categoryscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryItem(
    onClick: (String) -> Unit,
    name: String,
    spendingAmount: Int,
    spendingSum: Double,
    modifier: Modifier = Modifier
) {
    var enabled by remember { mutableStateOf(true) }
    val alpha: Float by animateFloatAsState(if (enabled) 1f else 0.95f)

    Column(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .graphicsLayer(scaleX = alpha, scaleY = alpha)
            .padding(end = 36.dp)
            .clickable { onClick(name) }
            .onPointerEvent(PointerEventType.Press) { enabled = false }
            .onPointerEvent(PointerEventType.Release) { enabled = true }
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = "łącznie wydano: %.2f zł".format(spendingSum),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = "ilość wydatków: $spendingAmount",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}