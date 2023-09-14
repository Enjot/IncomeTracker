package ui.rightcontent

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LimitList(
    list: List<LimitItem>,
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = shape,
        modifier = modifier
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

}
