package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onItemClick: (String) -> Unit,
    onAddButtonClick: (String) -> Unit,
    categories: List<MutableMap.MutableEntry<String, Pair<Int, Double>>>
) {
    val stateVertical = rememberLazyGridState()
    var dialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Column {
                Text(
                    text = "Zarządzaj kategoriami",
                    style = MaterialTheme.typography.displayLarge,
                )
                Spacer(modifier = Modifier.height(48.dp))
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(250.dp),
                    state = stateVertical
                ) {
                    items(categories) {
                        CategoryItem(
                            onClick = onItemClick,
                            name = it.key,
                            spendingAmount = it.value.first,
                            spendingSum = it.value.second
                        )
                    }
                }
            }
            ExtendedFloatingActionButton(
                text = { Text("Dodaj kategorię") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                onClick = { dialog = true },
                expanded = !stateVertical.canScrollBackward,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(48.dp)
            )
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(stateVertical),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .wrapContentHeight()
            )

            if (dialog) {
                AlertDialog(
                    onDismissRequest = { dialog = false },
                    modifier = Modifier.wrapContentHeight().padding(vertical = 24.dp).clip(RoundedCornerShape(24.dp))
                ) {

                    val focusRequester = remember { FocusRequester() }
                    
                    var name by remember { mutableStateOf("") }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(48.dp)

                    ) {
                        Text(
                            text = "Dodaj kategorię",
                            style = MaterialTheme.typography.displaySmall
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = name,
                            label = { Text("Nazwa") },
                            onValueChange = { name = it },
                            modifier = Modifier
                                .padding(12.dp)
                                .focusable()
                                .focusRequester(focusRequester)
                        )
                        Spacer(modifier = Modifier.height(36.dp))
                        Button(
                            onClick = {
                                onAddButtonClick(name)
                                dialog = false
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Dodaj")
                        }
                    }
                    
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }

        }
    }
}

@Composable
fun CategoryItem(
    onClick: (String) -> Unit,
    name: String,
    spendingAmount: Int,
    spendingSum: Double,
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .padding(end = 36.dp)
            .clickable { onClick(name) }
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = "łącznie wydano: %.2f zł".format(spendingSum),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = "ilość wydatków: $spendingAmount",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}