package ui.rightcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleLimitCard(
    item: LimitItem,
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
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Column {
                Text(item.limitName)
                Spacer(modifier = Modifier.height(12.dp))
                Text("${item.currentExpenses} zł")
            }
            Text(
                text = "${item.plannedExpenses} zł",
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }
    }
}
