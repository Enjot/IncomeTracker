package ui.rightcontent
import LimitItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
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
            var isInEditMode by remember { mutableStateOf(false) }

            var limitName by remember { mutableStateOf("") }
            var currentExpenses by remember { mutableStateOf("") }
            var plannedExpenses by remember { mutableStateOf("") }


            if (isInEditMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isInEditMode = !isInEditMode }
                    ) {
                        OutlinedTextField(
                            value = limitName,
                            label = { Text("nazwa") },
                            onValueChange = { limitName = it },
                            singleLine = true,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                        OutlinedTextField(
                            value = currentExpenses,
                            label = { Text("kategoria") },
                            onValueChange = { currentExpenses = it },
                            singleLine = true,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                        OutlinedTextField(
                            value = plannedExpenses,
                            label = { Text("limit") },
                            onValueChange = { plannedExpenses = it },
                            singleLine = true,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            isInEditMode = false
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = null
                        )
                    }

                }
            } else {
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
                                onClick = {
                                    isInEditMode = !isInEditMode
                                    limitName = it.limitName
                                    currentExpenses = it.currentExpenses.toString()
                                    plannedExpenses = it.plannedExpenses.toString()
                                },
                                item = it,
                                actualProgress = (it.currentExpenses.toFloat() / it.plannedExpenses.toFloat()),
                                modifier = Modifier.padding(4.dp)
                            )
                        }

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
