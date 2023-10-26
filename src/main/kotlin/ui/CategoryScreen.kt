package ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategoryScreen(
    categories: Map<String, Pair<Int, Double>>
) {
    val categoriesList by remember { mutableStateOf(categories.entries.toList()) }
    val stateVertical = rememberLazyGridState()
    
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
                    columns = GridCells.FixedSize(250.dp),
                    state = stateVertical
                ) {
                    items(categoriesList) {
                        CategoryItem(
                            it.key,
                            it.value.first,
                            it.value.second
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
                onClick = {},
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
        }

    }
}

@Composable
fun CategoryItem(
    name: String,
    spendingAmount: Int,
    spendingSum: Double,
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .padding(end = 36.dp)
            .clickable { }
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