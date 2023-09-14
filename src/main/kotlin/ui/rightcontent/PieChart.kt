package ui.rightcontent

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PieChart(
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(vertical = 12.dp)
        ) {
            Image(
                painter = painterResource("drawable/PieChartPlaceHolder.png"),
                contentDescription = null
            )
            Box() {
                val listOfCategory = listOf(
                    "moda", "żywność", "transport", "elektronika",
                    "miesieczne opłaty", "podatki", "czynsz"
                )
                val stateVertical = rememberLazyListState()
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    userScrollEnabled = true,
                    state = stateVertical,
                    modifier = Modifier
                        .padding(
                            end = if (stateVertical.canScrollForward || stateVertical.canScrollBackward) 12.dp else 0.dp
                        )
                ) {
                    item {
                        listOfCategory.forEach {
                            Text(it)
                        }
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(stateVertical),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )

            }

        }
    }
}
