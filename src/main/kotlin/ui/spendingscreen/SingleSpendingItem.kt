package ui.spendingscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Spending

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleSpendingItem(
    onItemClick: (Long) -> Unit,
    item: Spending,
    modifier: Modifier = Modifier
) {
    var enabled by remember { mutableStateOf(true) }
    val alpha: Float by animateFloatAsState(if (enabled) 1f else 0.95f)
    
    Row(
        modifier = modifier
            .width(300.dp)
            .height(90.dp)
            .graphicsLayer(scaleX = alpha, scaleY = alpha)
            .padding(end = 36.dp)
            .clickable { onItemClick(item.id) }
            .onPointerEvent(PointerEventType.Press) { enabled = false }
            .onPointerEvent(PointerEventType.Release) { enabled = true }
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(4.dp)
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.category,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Text(
                text = item.date,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "%.2f zł".format(item.amount),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}
