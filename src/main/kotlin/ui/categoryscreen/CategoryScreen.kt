package ui.categoryscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryScreen(
    model: CategoryScreenModel
) {

    val categories = model.categoriesSummary.collectAsState(emptyList())
    val stateVertical = rememberLazyGridState()
    var dialog by remember { mutableStateOf(false) }
    var showScrollbar by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

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
                        text = "Zarządzaj kategoriami",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.weight(1f).clickable {}
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.BottomEnd)
                            .padding(vertical = 24.dp, horizontal = 36.dp)
                    ) {
                        val width = 200.dp
                        CategorySortPicker(
                            onSortClick = { type -> model.setSortType(type) },
                            value = model.sortType.value.visibleName
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(250.dp),
                    state = stateVertical
                ) {
                    items(categories.value) { category ->
                        CategoryItem(
                            { name -> model.delete(name) },
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
            ExtendedFloatingActionButton(
                text = { Text("Dodaj kategorię") },
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
            if (dialog) {
                AlertDialog(
                    onDismissRequest = { dialog = false },
                    modifier = Modifier.wrapContentHeight().padding(vertical = 24.dp)
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
                                onClick = {
                                    dialog = false
                                }
                            ) {
                                Text("Anuluj")
                            }
                            Spacer(modifier = Modifier.width(100.dp))
                            Button(
                                onClick = {
                                    model.insert(name)
                                    dialog = false
                                }
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
    }
}

