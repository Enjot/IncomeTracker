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
    val sortedFilteredSpendingsState = screenModel.sortedFilteredSpendings.collectAsState(emptyList())
    val allCategories = screenModel.allCategories.collectAsState(emptyList())
    val sortedCategoriesState = screenModel.sortedCategories.collectAsState(emptyList())
    var currentDestination by remember { mutableStateOf(Destination.SPENDINGS) }
    val dateFilter = screenModel.spendingDateFilter.collectAsState()
    val currentLimits = screenModel.currentLimits.collectAsState(emptyList())
    val limitDataFilter = screenModel.limitDateFilter.collectAsState()

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
                            onCategoryClick = { filter -> screenModel.selectSortedCategory(filter) },
                            onResetDateFilter = { screenModel.resetDateFilter() },
                            setDateFilter = { month, year, isFiltered ->
                                screenModel.setSpendingDateFilter(
                                    month,
                                    year,
                                    isFiltered
                                )
                            },
                            chosenSortType = screenModel.spendingSortType.value,
                            spendings = sortedFilteredSpendingsState.value,
                            dateFilter = dateFilter.value,
                            categories = allCategories.value.filter { it.isVisible.toInt() == 1 },
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    Destination.CATEGORIES -> {
                        CategoryScreen(
                            onItemClick = { name -> screenModel.setHiddenCategory(name) },
                            onAddButtonClick = { name -> screenModel.insertCategory(name) },
                            onSortClick = { type -> screenModel.setCategorySortType(type) },
                            categories = sortedCategoriesState.value,
                            chosenSortType = screenModel.categorySortType.value
                        )
                    }

                    Destination.LIMITS -> {
                        LimitScreen(
                            onAddButtonClick = { category, amount -> screenModel.insertLimit(category, amount) },
                            limit = currentLimits.value,
                            category = allCategories.value.filter { it.isVisible.toInt() == 1 },
                            dateFilter = limitDataFilter.value,
                            setLimitDateFilter = { month, year ->
                                screenModel.setLimitDateFilter(
                                    month,
                                    year
                                )
                            }
                        )
                    }

                    Destination.CHARTS -> {
                        Surface(modifier = Modifier.fillMaxSize()) { }
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