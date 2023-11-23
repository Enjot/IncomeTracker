package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.spendingscreen.SpendingScreen

// root composable function

@Composable
fun HomeScreen(
    screenModel: HomeScreenModel, modifier: Modifier = Modifier
) {
    val spendings = screenModel.spendings.collectAsState(emptyList())
    val categories = screenModel.categories.collectAsState(emptyList())
    val categoriesSummary = screenModel.categoriesSummary.collectAsState(emptyList())
    var currentDestination by remember { mutableStateOf(Destination.SPENDINGS) }
    val dateFilter = screenModel.filterSpendingByDate.collectAsState()
    val limits = screenModel.limits.collectAsState(emptyList())
    val limitDateFilter = screenModel.filterLimitByDate.collectAsState()
    val chartDateFilter = screenModel.statisticFilter.collectAsState()
    val chartStatistics = screenModel.currentMonthStatstics.collectAsState(emptyMap<String,Double>())

    Surface {
        Row(
            Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                NavigationRailItem(
                    selected = currentDestination == Destination.SPENDINGS,
                    onClick = { currentDestination = Destination.SPENDINGS },
                    label = { Text("Wydatki") },
                    icon = {
                        Icon(
                            if (currentDestination == Destination.SPENDINGS) {
                                painterResource("drawable/icons/spendingFilled.svg")
                            } else {
                                painterResource("drawable/icons/spending.svg")
                            },
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.CATEGORIES,
                    onClick = { currentDestination = Destination.CATEGORIES },
                    label = { Text("Kategorie") },
                    icon = {
                        Icon(
                            if (currentDestination == Destination.CATEGORIES) {
                                painterResource("drawable/icons/categoryFilled.svg")
                            } else {
                                painterResource("drawable/icons/category.svg")
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.LIMITS,
                    onClick = { currentDestination = Destination.LIMITS },
                    label = { Text("Limity") },
                    icon = {
                        Icon(
                            if (currentDestination == Destination.LIMITS) {
                                painterResource("drawable/icons/limitsFilled.svg")
                            } else {
                                painterResource("drawable/icons/limits.svg")
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.CHARTS,
                    onClick = { currentDestination = Destination.CHARTS },
                    label = { Text("Statystyki") },
                    icon = {
                        Icon(
                            if (currentDestination == Destination.CHARTS) {
                                painterResource("drawable/icons/chartsFilled.svg")
                            } else {
                                painterResource("drawable/icons/charts.svg")
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.SETTINGS,
                    onClick = { currentDestination = Destination.SETTINGS },
                    label = { Text("Ustawienia") },
                    icon = {
                        Icon(
                            if (currentDestination == Destination.SETTINGS) {
                                painterResource("drawable/icons/settingsFilled.svg")
                            } else {
                                painterResource("drawable/icons/settings.svg")
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
            AnimatedContent(
                targetState = currentDestination,
                modifier = Modifier
                    .shadow(128.dp, shape = RoundedCornerShape(topStart = 64.dp, bottomStart = 64.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) { destination ->
                when (destination) {
                    Destination.SPENDINGS -> {
                        SpendingScreen(
                            onItemClick = { id -> screenModel.deleteSpending(id) },
                            onAddClick = { name, amount, category ->
                                screenModel.insertSpending(
                                    name,
                                    amount,
                                    category
                                )
                            },
                            onSortClick = { type -> screenModel.setSpendingSortType(type) },
                            onCategoryClick = { filter -> screenModel.setSpendingFilterByCategory(filter) },
                            onResetDateFilter = { screenModel.resetSpendingDateFilter() },
                            setDateFilter = { month, year, isFiltered ->
                                screenModel.setFilterSpendingByDate(
                                    month,
                                    year,
                                    isFiltered
                                )
                            },
                            chosenSortType = screenModel.sortSpending.value,
                            spendings = spendings.value,
                            dateFilter = dateFilter.value,
                            categories = categories.value,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    Destination.CATEGORIES -> {
                        CategoryScreen(
                            onItemClick = { name -> screenModel.deleteCategory(name) },
                            onAddButtonClick = { name -> screenModel.insertCategory(name) },
                            onSortClick = { type -> screenModel.setCategorySortType(type) },
                            categories = categoriesSummary.value,
                            chosenSortType = screenModel.sortCategory.value
                        )
                    }

                    Destination.LIMITS -> {
                        LimitScreen(
                            onAddButtonClick = { category, amount -> screenModel.insertLimit(category, amount) },
                            limit = limits.value,
                            category = categories.value,
                            dateFilter = limitDateFilter.value,
                            setLimitDateFilter = { month, year ->
                                screenModel.setFilterLimitByDate(
                                    month,
                                    year
                                )
                            }
                        )
                    }

                    Destination.CHARTS -> {
                        ChartScreen(
                            monthStatistic = chartStatistics.value,
                            dateFilter = chartDateFilter.value,
                            setStatisticsFilter = {month, year ->
                                screenModel.setChartByDate(
                                    month,
                                    year
                                )
                            }
                        )
                    }

                    Destination.SETTINGS -> {
//                            Surface(modifier = Modifier.fillMaxSize()) { }
                    }
                }
            }
        }
    }
}

enum class Destination {
    SPENDINGS, CATEGORIES, LIMITS, CHARTS, SETTINGS
}