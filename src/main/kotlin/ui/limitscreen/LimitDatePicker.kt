package ui.limitscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun LimitDatePicker(
    onClick: (Int, Int) -> Unit,
    selectedMonth: Int,
    selectedYear: Int,
    modifier: Modifier = Modifier
) {
    val monthWidth = 190.dp
    val yearWidth = 140.dp
    var monthExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    val monthArrow: Float by animateFloatAsState(
        targetValue = if (monthExpanded) 180F else 0F,
        animationSpec = spring(
            stiffness = 1000f
        )
    )
    val yearArrow: Float by animateFloatAsState(
        targetValue = if (yearExpanded) 180F else 0F,
        animationSpec = spring(
            stiffness = 1000f
        )
    )
    var currentMonth by remember { mutableStateOf(monthNames[selectedMonth - 1]) }
    var currentYear by remember { mutableStateOf(selectedYear) }

    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(yearExpanded) {
        coroutineScope.launch {
            lazyColumnState.scrollToItem(years.indexOf(selectedYear) - 5)
        }
    }
    
    Row {
        Box(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = currentMonth,
                onValueChange = { },
                singleLine = true,
                readOnly = true,
                leadingIcon = {
                    Icon(
                        painterResource("drawable/icons/month.svg"),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(monthArrow)
                    )
                },
                modifier = Modifier
                    .width(monthWidth)

            )
            Spacer(
                modifier = Modifier
                    .width(monthWidth)
                    .height(56.dp)
                    .clickable { monthExpanded = !monthExpanded }
            )
        }
        DropdownMenu(
            expanded = monthExpanded,
            onDismissRequest = { monthExpanded = false },
            modifier = Modifier.width(monthWidth)
        ) {
            monthNames.forEachIndexed { index, monthName ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = monthName,
                            textAlign = TextAlign.Center,
                            color = if (currentMonth == monthName) MaterialTheme.colorScheme.primary else Color.Unspecified,
                            fontWeight = if (currentMonth == monthName) FontWeight.Bold else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        currentMonth = monthNames[index]
                        run { onClick(index + 1, currentYear) }
                        monthExpanded = false
                    }
                )
            }
        }
        Spacer(modifier.width(32.dp))
        Box {
            Box(
                modifier = modifier
            ) {
                OutlinedTextField(
                    value = currentYear.toString(),
                    onValueChange = { },
                    singleLine = true,
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            painterResource("drawable/icons/year.svg"),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(yearArrow)
                        )
                    },
                    modifier = Modifier
                        .width(yearWidth)

                )
                Spacer(
                    modifier = Modifier
                        .width(yearWidth)
                        .height(56.dp)
                        .clickable { yearExpanded = !yearExpanded }
                )
            }
            DropdownMenu(
                expanded = yearExpanded,
                onDismissRequest = { yearExpanded = false },
                modifier = Modifier.width(yearWidth).padding()
            ) {
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier.width(yearWidth).height(520.dp)
                ) {
                    items(items = years.toList()) { year ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = year.toString(),
                                    textAlign = TextAlign.Center,
                                    color = if (currentYear == year) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                    fontWeight = if (currentYear == year) FontWeight.Bold else null,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                currentYear = year
                                run { onClick(monthNames.indexOf(currentMonth) + 1, year) }
                                yearExpanded = false
                            }
                        )
                    }
                }
            }
        }

    }
}

val years: IntRange = 1950..2100
val monthNames: List<String> = listOf(
    "Styczeń",
    "Luty",
    "Marzec",
    "Kwiecień",
    "Maj",
    "Czerwiec",
    "Lipiec",
    "Sierpień",
    "Wrzesień",
    "Październik",
    "Listopad",
    "Grudzień"
)