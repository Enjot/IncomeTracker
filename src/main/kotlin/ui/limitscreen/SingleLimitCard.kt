package ui.limitscreen

import data.ViewModels.CurrentLimit
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleLimitCard(
    onEditClick: (String, Double, String) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    item: CurrentLimit
) {

    val ratio = (item.currentAmount / item.limitAmount).toFloat()
    val progressColor =
        if (ratio < 0.9f) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error
    var editDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clickable { editDialog = true }
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

    if (editDialog) {
        AlertDialog(
            onDismissRequest = { editDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier.wrapContentHeight().padding(vertical = 24.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            EditLimit(
                { name, amount, date -> onEditClick(name, amount, date) },
                { name, date -> onDeleteClick(name, date) },
                { editDialog = !editDialog },
                item
            )
        }
    }
}