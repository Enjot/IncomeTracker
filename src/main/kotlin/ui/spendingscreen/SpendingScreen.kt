package ui.spendingscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.sqldelight.Category
import com.example.sqldelight.Spending
import kotlinx.coroutines.launch
import ui.DateFilter
import ui.SpendingSortType
import ui.monthNames

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SpendingScreen(
    onItemClick: (Long) -> Unit,
    onSortClick: (SpendingSortType) -> Unit,
    onAddClick: (String, Double, Category) -> Unit,
    onCategoryClick: (String) -> Unit,
    onResetDateFilter: () -> Unit,
    setDateFilter: (Int, Int, Boolean) -> Unit,
    chosenSortType: SpendingSortType,
    categories: List<Category>,
    spendings: List<Spending>,
    dateFilter: DateFilter,
    modifier: Modifier = Modifier
) {
    val stateVertical = rememberLazyGridState()
    var dialog by remember { mutableStateOf(false) }
    var showScrollbar by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Enter) { showScrollbar = true }
            .onPointerEvent(PointerEventType.Exit) { showScrollbar = false }
        
    ) {
        Box(modifier = Modifier.padding(start = 36.dp)) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Zarządzaj wydatkami",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.weight(1f).clickable { }
                    )
                    // Had to wrap it in Box for normal dropdown menu behaviour
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        SortPicker(
                            onSortClick = onSortClick,
                            value = chosenSortType.sortType,
                        )
                    }
                    OutlinedButton(
                        onClick = { showBottomSheet = !showBottomSheet },
                        modifier = Modifier
                            .wrapContentSize(Alignment.BottomEnd)
                            .padding(top = 24.dp, bottom = 24.dp, end = 36.dp)
                    ) {
                        Text("Filtruj")
                        Spacer(modifier = Modifier.width(24.dp))
                        Icon(
                            painterResource("drawable/icons/filter.svg"),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(300.dp),
                    state = stateVertical
                ) {
                    items(spendings) {
                        SingleSpendingItem(
                            onItemClick,
                            it
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(128.dp))

                    }
                }
            }
            ExtendedFloatingActionButton(
                text = { Text("Dodaj wydatek") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                onClick = { dialog = true },
                expanded = !stateVertical.canScrollBackward || !stateVertical.canScrollForward,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(48.dp)
            )
            AnimatedVisibility(
                visible = showScrollbar,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(stateVertical),
                    style = ScrollbarStyle(
                        minimalHeight = 16.dp,
                        thickness = 8.dp,
                        shape = RoundedCornerShape(4.dp),
                        hoverDurationMillis = 300,
                        unhoverColor = MaterialTheme.colorScheme.outlineVariant,
                        hoverColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .wrapContentHeight()
                )
            }
        }
        if (dialog) {
            AlertDialog(
                onDismissRequest = { dialog = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                modifier = Modifier
                    .width(1000.dp)
                    .padding(vertical = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AddSpending(
                    onAddClick,
                    { dialog = !dialog },
                    categories
                )
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {

            var selectedMonth by remember { mutableStateOf(dateFilter.selectedMonth) }
            var selectedYear by remember { mutableStateOf(dateFilter.selectedYear) }
            var isFiltered by remember { mutableStateOf(dateFilter.isFiltered) }
            
            Column {
                DateFilterSelector(
                    monthsNames = monthNames,
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear,
                    onYearSelect = { selectedYear = it.toInt() },
                    onMonthSelect = { selectedMonth = it.toInt() },
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Włącz filtrowanie po dacie")
                    Spacer(modifier = Modifier.width(24.dp))
                    Switch(
                        checked = isFiltered,
                        onCheckedChange = { isFiltered = !isFiltered },
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(200.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    item {
                        Text(
                            text = "Pokaż wszystkie",
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    onCategoryClick("")
                                }
                        )
                    }
                    items(categories) {
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    onCategoryClick(it.name)
                                }
                        )
                    }
                }
                Row {
                    TextButton(
                        onClick = {
                            onCategoryClick("")
                            onResetDateFilter()
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text("Wyczyść filtrowanie")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            setDateFilter(selectedMonth, selectedYear, isFiltered)
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                            
                        },
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text("Filtruj")
                    }
                }
            }
        }
    }
}