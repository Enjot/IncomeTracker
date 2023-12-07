package ui.categoryscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import ui.utils.AddButton
import ui.utils.ScreenContent
import ui.utils.Scrollbar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
    model: CategoryScreenModel
) {
    val categories = model.categoriesSummary.collectAsState(emptyList())
    val stateVertical = rememberLazyGridState()
    var showDialog by remember { mutableStateOf(false) }
    var showScrollbar by remember { mutableStateOf(false) }

    ScreenContent(
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) { showScrollbar = true }
            .onPointerEvent(PointerEventType.Exit) { showScrollbar = false }
    ) {
        Column {
            HeaderRow(
                sortName = model.sortType.value.visibleName,
                onSortClick = { type -> model.setSortType(type) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesGrid(
                elements = categories.value,
                state = stateVertical,
                onClick = { name -> model.delete(name) }
            )
        }
        AddButton(
            text = "Dodaj kategorię",
            expanded = !stateVertical.canScrollBackward || !stateVertical.canScrollForward,
            modifier = Modifier.align(Alignment.BottomEnd).padding(48.dp),
            onClick = { showDialog = true }
        )
        Scrollbar(
            showScrollbar,
            rememberScrollbarAdapter(stateVertical),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        AddDialog(
            showDialog = showDialog,
            onDismissRequest = { showDialog = false },
            onAdd = { name ->
                model.insert(name)
                showDialog = false
            }
        )
    }
}

@Composable
private fun HeaderRow(
    onSortClick: (CategorySortType) -> Unit,
    sortName: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "Kategorie",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.BottomEnd)
                .padding(vertical = 24.dp, horizontal = 36.dp)
        ) {
            CategorySortPicker(
                onSortClick = onSortClick,
                value = sortName
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            modifier = modifier.wrapContentHeight().padding(vertical = 24.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {

            val focusRequester = remember { FocusRequester() }
            var name by remember { mutableStateOf("") }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(40.dp)

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
                        .focusable()
                        .focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(36.dp))
                Row {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text("Anuluj")
                    }
                    Spacer(modifier = Modifier.width(100.dp))
                    Button(
                        onClick = { onAdd(name) }
                    ) {
                        Text("Dodaj")
                    }
                }
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesGrid(
    elements: List<MutableMap.MutableEntry<String, Pair<Int, Double>>>,
    state: LazyGridState,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(250.dp),
        state = state,
        modifier = modifier
    ) {
        items(elements, key = { it.key }) { category ->
            CategoryElement(
                onClick,
                name = category.key,
                spendingAmount = category.value.first,
                spendingSum = category.value.second,
                modifier = Modifier.animateItemPlacement()
            )
        }
        item {
            Spacer(modifier = Modifier.height(128.dp))
        }
    }
}