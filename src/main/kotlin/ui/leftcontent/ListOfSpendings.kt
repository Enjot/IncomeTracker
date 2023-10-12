package ui.leftcontent

import HomeScreen
import SpendingItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Spending
import ui.rightcontent.shape

@Composable
fun ListOfSpending(
    onAddClick: (String, Double) -> Unit,
    onDeleteClick: (Long) -> Unit,
    list: List<Spending>,
    modifier: Modifier = Modifier
) {
    var isInEditMode by remember { mutableStateOf(false) }

    var spendingName by remember { mutableStateOf("") }
    var spendingCategory by remember { mutableStateOf("") }
    var spendingAmount by remember { mutableStateOf("") }
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = shape,
        modifier = modifier
    ) {

        // use box to stack elements, in our case: list, scrollbar and floating action button

        Box(modifier) {


            if (isInEditMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = { isInEditMode = !isInEditMode },
                        shape = RoundedCornerShape(100),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        OutlinedTextField(
                            value = spendingName,
                            label = { Text("nazwa") },
                            onValueChange = { spendingName = it },
                            singleLine = true,
                            modifier = Modifier
                                .padding(12.dp)
                        )
//                        OutlinedTextField(
//                            value = spendingCategory,
//                            label = { Text("kategoria") },
//                            onValueChange = { spendingCategory = it },
//                            singleLine = true,
//                            modifier = Modifier
//                                .padding(12.dp)
//                        )
                        OutlinedTextField(
                            value = spendingAmount,
                            label = { Text("Kwota") },
                            onValueChange = { spendingAmount = it },
                            singleLine = true,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }
                    Button(
                        onClick = {
                            onAddClick(spendingName, spendingAmount.toDouble())
                            spendingName = ""
                            spendingAmount = ""
                            isInEditMode = !isInEditMode
                        },
                        shape = RoundedCornerShape(100),
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
                                onClick = onDeleteClick,
                                modifier = Modifier
                                    .padding(4.dp)
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
                Button(
                    onClick = { isInEditMode = !isInEditMode },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp)
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
}