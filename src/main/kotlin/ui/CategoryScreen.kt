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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoryScreen(
    onItemClick: (String) -> Unit,
    onAddButtonClick: (String) -> Unit,
    categories: List<MutableMap.MutableEntry<String, Pair<Int, Double>>>
) {
    val stateVertical = rememberLazyGridState()
    var dialog by remember { mutableStateOf(false) }
    var showScrollbar by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var currentSortType by remember { mutableStateOf("od A do Z") }
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
                        modifier = Modifier.weight(1f).clickable{}
                    )
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.BottomEnd)
                            .padding(vertical = 24.dp, horizontal = 36.dp)
                    ) {
                        Box {
                            OutlinedTextField(
                                value = "Sortuj $currentSortType",
                                onValueChange = { },
                                singleLine = true,
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .rotate(arrowOrientation)
                                    )
                                },
                                modifier = Modifier
                                    .width(250.dp)

                            )
                            Spacer(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(55.dp)
                                    .clickable { expanded = !expanded }
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.width(250.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text("od najtańszych") },
                                onClick = {
                                    currentSortType = "od najtańszych"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("od najdroższych") },
                                onClick = {
                                    currentSortType = "od najdroższych"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("od najnowszych") },
                                onClick = {
                                    currentSortType = "od najnowszych"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("od najstarszych") },
                                onClick = {
                                    currentSortType = "od najstarszych"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("od A do Z") },
                                onClick = {
                                    currentSortType = "od A do Z"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("od Z do A") },
                                onClick = {

                                    currentSortType = "od Z do A"
                                    expanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = null
                                    )
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