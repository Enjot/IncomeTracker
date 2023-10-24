package ui.leftcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Spending

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleSpendingCard(
    item: Spending,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(item.id) },
        shape = RoundedCornerShape(28.dp),
        elevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        // elements inside card of single spending item
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Column {
                Text("${item.name} ${item.category}")
                Spacer(modifier = Modifier.height(12.dp))
                Text("id: ${item.id}")
            }
            Column {
                Text(
                    text = item.date,
                    modifier = Modifier
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "${item.amount} zł",
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }
    }
}