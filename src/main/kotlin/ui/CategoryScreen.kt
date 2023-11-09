package ui

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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
    onItemClick: (String) -> Unit,
    onAddButtonClick: (String) -> Unit,
    onSortClick: (CategorySortType) -> Unit,
    categories: List<MutableMap.MutableEntry<String, Pair<Int, Double>>>,
    chosenSortType: CategorySortType
) {
    val stateVertical = rememberLazyGridState()
    var dialog by remember { mutableStateOf(false) }
    var showScrollbar by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val arrowOrientation: Float by animateFloatAsState(
        targetValue = if (expanded) 180F else 0F,
        animationSpec = spring(
            stiffness = 1000f
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .onPointerEvent(PointerEventType.Enter) { showScrollbar = true }
            .onPointerEvent(PointerEventType.Exit) { showScrollbar = false }
    ) {
        Box {
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
                        val width = 225.dp
                        Box {
                            OutlinedTextField(
                                value = chosenSortType.sortType,
                                onValueChange = { },
                                singleLine = true,
                                readOnly = true,
                                leadingIcon = {
                                    Icon(
                                        painterResource("drawable/icons/sorting.svg"),
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .rotate(arrowOrientation)
                                    )
                                },
                                modifier = Modifier
                                    .width(width)

                            )
                            Spacer(
                                modifier = Modifier
                                    .width(width)
                                    .height(56.dp)
                                    .clickable { expanded = !expanded }
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.width(width)
                        ) {
                            DropdownMenuItem(
                                text = { Text(CategorySortType.NAME_INC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.NAME_INC)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(CategorySortType.NAME_DEC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.NAME_DEC)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(CategorySortType.SUM_INC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.SUM_INC)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(CategorySortType.SUM_DEC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.SUM_DEC)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(CategorySortType.AMOUNT_INC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.AMOUNT_INC)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(CategorySortType.AMOUNT_DEC.sortType) },
                                onClick = {
                                    onSortClick(CategorySortType.AMOUNT_DEC)
                                    expanded = false
                                }
                            )

                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                    modifier = Modifier.wrapContentHeight().padding(vertical = 24.dp).clip(RoundedCornerShape(24.dp))
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
                                    onAddButtonClick(name)
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