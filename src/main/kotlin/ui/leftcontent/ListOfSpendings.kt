package ui.leftcontent

import SpendingItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.rightcontent.shape

@Composable
fun ListOfSpending(
    list: List<SpendingItem>,
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = shape,
        modifier = modifier
    ) {

        // use box to stack elements, in our case: list, scrollbar and floating action button

        Box(modifier) {

            val stateVertical = rememberLazyListState()

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp),
                userScrollEnabled = true,
                state = stateVertical,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(
                        end = if (stateVertical.canScrollForward || stateVertical.canScrollBackward) 8.dp else 0.dp
                    )

            ) {
                item {
                    // use list of items and show every element
                    list.forEach {
                        SingleSpendingCard(
                            item = it,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    // add spacer at the end of list so floating action button doesn't cover last item
                    /* TODO make it like spacer height depends on button height */
                    Spacer(modifier = Modifier.height(56.dp))
                }
            }
            // rest of elements in box (scrollbar and floating action button)
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(stateVertical),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                // elements inside button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dodaj wydatek")
                }

            }
        }
    }
}