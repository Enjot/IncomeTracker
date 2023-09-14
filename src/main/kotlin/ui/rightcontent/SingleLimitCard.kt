package ui.rightcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleLimitCard(
    item: LimitItem,
    actualProgress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {},
        shape = RoundedCornerShape(28.dp),
        elevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Column {
                Text(item.limitName)
                Spacer(modifier = Modifier.height(12.dp))
                Text("${item.currentExpenses} zł")
            }
            LinearProgressIndicator(
                progress = actualProgress,
                backgroundColor = Color.LightGray,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = "${item.plannedExpenses} zł",
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }
    }
}
