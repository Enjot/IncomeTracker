package ui.rightcontent

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


val shape = RoundedCornerShape(28.dp)

@Composable
fun RightContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(24.dp)
    ) {
        /* TODO button to change theme */
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 4.dp,
            shape = shape,
            modifier = Modifier
                .weight(2f)
                .fillMaxSize()
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
        Spacer(modifier = Modifier.height(48.dp))
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 4.dp,
            shape = shape,
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
        ) {
            LimitList(
                listOfLimitItem,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
    }
}

data class LimitItem(
    val limitName: String,
    val currentExpenses: Int,
    val plannedExpenses: Int
)

val listOfLimitItem = listOf(
    LimitItem(
        "żywność",
        13,
        1000
    ),

    LimitItem(
        "rozrywka",
        20,
        300
    ),

    LimitItem(
        "transport",
        30,
        150
    )
)

@Composable
fun LimitList(
    list: List<LimitItem>,
    modifier: Modifier = Modifier
) {
    Box(modifier) {

        val stateVertical = rememberLazyListState()

        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            userScrollEnabled = true,
            state = stateVertical,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    end = if (stateVertical.canScrollForward || stateVertical.canScrollBackward) 8.dp else 0.dp
                )
        ) {
            item {
                list.forEach {
                    SingleLimitCard(
                        item = it,
                        modifier = Modifier.padding(4.dp)
                    )
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



