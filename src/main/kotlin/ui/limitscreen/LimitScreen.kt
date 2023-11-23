package ui.limitscreen

import Validator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.sqldelight.Category
import ui.CurrentLimit
import ui.DateFilter
import ui.utils.LimitDatePicker

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LimitScreen(
    onAddButtonClick: (Category, Double) -> Unit,
    setLimitDateFilter: (Int, Int) -> Unit,
    category: List<Category>,
    limit: List<CurrentLimit>,
    dateFilter: DateFilter
) {

    var showScrollbar by remember { mutableStateOf(false) }
    val stateVertical = rememberLazyListState()
    var dialog by remember { mutableStateOf(false) }
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Zarządzaj Limitami",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.weight(1f).clickable { }
                    )
                    Box(
                        modifier = Modifier.padding(vertical = 24.dp, horizontal = 36.dp)
                    ) {
                        LimitDatePicker(
                            selectedMonth = dateFilter.selectedMonth,
                            selectedYear = dateFilter.selectedYear,
                            onClick = { month, year -> setLimitDateFilter(month, year) },
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxSize().padding(end = 36.dp)) {
                    Spacer(modifier = Modifier.weight(1f))
                    LazyColumn(
                        state = stateVertical,
                        modifier = Modifier
                            .weight(6f)
                    ) {
                        items(limit) {
                            SingleLimitCard(it)
                        }
                        item { Spacer(modifier = Modifier.height(128.dp)) }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
            ExtendedFloatingActionButton(
                text = { Text("Ustal limit") },
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
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    ),
                    modifier = Modifier
                        .width(1000.dp)
                        .padding(vertical = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    LimitDialog(
                        onAddButtonClick,
                        { dialog = !dialog },
                        category
                    )
                }
            }
        }
    }
}

@Composable
fun SingleLimitCard(
    item: CurrentLimit
) {

    val ratio = (item.currentAmount / item.limitAmount).toFloat()
    val progressColor =
        if (ratio < 0.9f) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.categoryName,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.currentDate,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                LinearProgressIndicator(
                    color = progressColor,
                    progress = ratio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(12.dp)
            ) {
                Text(
                    text = "%.2f zł".format(item.currentAmount),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "%.2f zł".format(item.limitAmount),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }

    }
}

@Composable
fun LimitDialog(
    onAddButtonClick: (Category, Double) -> Unit,
    onCloseDialog: () -> Unit,
    category: List<Category>
) {

    var amount by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("Wybierz kategorię") }
    var chosenCategory by remember { mutableStateOf(0) }

    var isError by remember { mutableStateOf(false) }

    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(48.dp)
                .fillMaxHeight()
                .wrapContentWidth()

        ) {
            Text(
                text = "Ustal limit",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = amount,
                isError = isError,
                label = { Text("Kwota") },
                onValueChange = { amount = it },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()
//                            .focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.height(36.dp))
            OutlinedTextField(
                value = categoryName,
                label = { Text("Kategria") },
                singleLine = true,
                readOnly = true,
                onValueChange = { },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()

//                            .focusRequester(focusRequester)
            )
            Button(
                onClick = {
                    isError = false
                    if(Validator.isValidAmount(amount)) {
                        onAddButtonClick(category[chosenCategory], amount.toDouble())
                        onCloseDialog()
                    } else isError = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Dodaj")
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(48.dp)
                .fillMaxHeight()
                .weight(1f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp)
            ) {
                items(category) {
                    Text(
                        text = it.name,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                chosenCategory = category.indexOf(it)
                                categoryName = it.name
                            }
                    )
                }
            }
        }
    }

}


