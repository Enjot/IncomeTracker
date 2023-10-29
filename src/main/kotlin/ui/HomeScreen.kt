package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// root composable function

@Composable
fun HomeScreen(
    screenModel: HomeScreenModel, modifier: Modifier = Modifier
) {
    val allSpendings = screenModel.allSpendings.collectAsState(emptyList())
    val allCategories = screenModel.allCategories.collectAsState(emptyList())
    val categories = screenModel.categories.collectAsState(emptyList())
    var currentDestination by remember { mutableStateOf(Destination.SPENDINGS) }

    Surface(
        color = MaterialTheme.colorScheme.surface, modifier = modifier
    ) {
        Row() {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
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
            when (currentDestination) {
                Destination.SPENDINGS -> {
                    SpendingScreen(
                        onItemClick = { id -> screenModel.deleteSpending(id) },
                        onAddClick = { name, amount, Category -> screenModel.insertSpending(name, amount, Category) },
                        spendings = allSpendings.value,
                        category = allCategories.value.filter { it.isDeleted.toInt() == 0 },
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                Destination.CATEGORIES -> {
                    CategoryScreen(
                        onItemClick = { name -> screenModel.deleteCategory(name) },
                        onAddButtonClick = { name -> screenModel.insertCategory(name) },
                        categories = categories.value
                    )
                }

                Destination.LIMITS -> {
                    LimitScreen()
                }

                Destination.CHARTS -> {
                    LimitScreen()
                }

                Destination.SETTINGS -> {
                    LimitScreen()
                }
            }
        }
    }
}

enum class Destination {
    SPENDINGS, CATEGORIES, LIMITS, CHARTS, SETTINGS
}