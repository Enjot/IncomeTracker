package ui.limitscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ui.utils.AddButton
import ui.utils.ScreenContent
import ui.utils.Scrollbar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LimitScreen(
    model: LimitScreenModel
) {

    val limit = model.limits.collectAsState(emptyList())
    val category = model.categories.collectAsState(emptyList())
    var showScrollbar by remember { mutableStateOf(false) }
    val stateVertical = rememberLazyListState()
    var dialog by remember { mutableStateOf(false) }
    
    ScreenContent(
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) { showScrollbar = true }
            .onPointerEvent(PointerEventType.Exit) { showScrollbar = false }
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Miesięczne limity",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 36.dp)
                ) {
                    LimitDatePicker(
                        selectedMonth = model.filter.value.selectedMonth,
                        selectedYear = model.filter.value.selectedYear,
                        onClick = { month, year -> model.setFilter(month, year, true) },
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
                    items(limit.value) {
                        SingleLimitCard(
                            { name, amount, date ->
                                model.update(
                                    name,
                                    amount,
                                    date
                                )
                            },
                            { name, date ->
                                model.delete(
                                    name,
                                    date
                                )
                            },
                            it
                        )
                    }
                    item { Spacer(modifier = Modifier.height(128.dp)) }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

        }
        AddButton(
            text = "Ustal limit",
            expanded = !stateVertical.canScrollBackward || !stateVertical.canScrollForward,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(48.dp)
        )
        Scrollbar(
            showScrollbar,
            rememberScrollbarAdapter(stateVertical),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
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
                    { category, amount -> model.insert(category, amount) },
                    { dialog = !dialog },
                    category.value
                )
            }
        }
    }
}