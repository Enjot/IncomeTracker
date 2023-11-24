package ui.limitscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SingleLimitCard(
    item: CurrentLimit
) {

    val ratio = (item.currentAmount / item.limitAmount).toFloat()
    val progressColor =
        if (ratio < 0.9f) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.categoryName,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.currentDate,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                LinearProgressIndicator(
                    color = progressColor,
                    progress = ratio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(12.dp)
            ) {
                Text(
                    text = "%.2f zł".format(item.currentAmount),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "%.2f zł".format(item.limitAmount),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }

    }
}