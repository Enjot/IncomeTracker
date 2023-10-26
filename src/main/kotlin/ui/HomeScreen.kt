package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// root composable function

@Composable
fun HomeScreen(
    screenModel: HomeScreenModel, modifier: Modifier = Modifier
) {

    val spendings = screenModel.allSpendings.collectAsState(emptyList())
    val categories = screenModel.allCategories.collectAsState(emptyList())
    val categoriesTesting = screenModel.categories.collectAsState(emptyMap<String, Pair<Int, Double>>())
    
    // blank surface that fill whole window and change color itself depending on theme
    Surface(
        color = MaterialTheme.colorScheme.surface, modifier = modifier
    ) {
        
        var currentDestination by remember { mutableStateOf(Destination.SPENDINGS) }
        
        Row() {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                NavigationRailItem(
                    selected = currentDestination == Destination.SPENDINGS,
                    onClick = { currentDestination = Destination.SPENDINGS },
                    label = { Text("Wydatki")},
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.CATEGORIES,
                    onClick = { currentDestination = Destination.CATEGORIES },
                    label = { Text("Kategorie")},
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
                NavigationRailItem(
                    selected = currentDestination == Destination.LIMITS,
                    onClick = { currentDestination = Destination.LIMITS },
                    label = { Text("Limity")},
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }
            when (currentDestination) {
                Destination.SPENDINGS -> {
                    SpendingScreen()
                }
                Destination.CATEGORIES -> {
                    CategoryScreen()
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