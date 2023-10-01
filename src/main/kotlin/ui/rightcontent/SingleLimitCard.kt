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
    onClick: () -> Unit,
    item: LimitItem,
    actualProgress: Float,
    modifier: Modifier = Modifier
) {


    Card(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        elevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
            ) {
                Text(item.limitName)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("${item.currentExpenses} zł")
                LinearProgressIndicator(
                    progress = actualProgress,
                    backgroundColor = Color.LightGray,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .height(8.dp)
                        .align(Alignment.Bottom)
                )

                Text("${item.plannedExpenses} zł")
            }

        }
    }
}
