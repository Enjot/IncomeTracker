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
                            painterResource("drawable/icons/spending.svg"),
                            contentDescription = null
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
                            painterResource("drawable/icons/category.svg"),
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
                            painterResource("drawable/icons/limit.svg"),
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
                        spendings = allSpendings.value,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                Destination.CATEGORIES -> {
                    CategoryScreen(
                        onAddClick = { name -> screenModel.insertCategory(name) },
                        categories = categories.value
                    )
                }

                Destination.LIMITS -> {
                    LimitScreen()
                }
            }
        }
    }
}

enum class Destination {
    SPENDINGS, CATEGORIES, LIMITS
}