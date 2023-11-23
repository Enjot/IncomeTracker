
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import data.DatabaseRepoImp
import data.DatabaseRepository
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.categoryscreen.CategoryScreenModel
import ui.chartscreen.ChartScreenModel
import ui.limitscreen.LimitScreenModel
import ui.spendingscreen.SpendingScreen
import ui.spendingscreen.SpendingScreenModel
import ui.theme.AppTheme
import java.awt.Dimension

val mainModule = module {

    singleOf(::DatabaseRepoImp) {
        bind<DatabaseRepository>()
    }

    factory { SpendingScreenModel(get()) }
    factory { CategoryScreenModel(get()) }
    factory { LimitScreenModel(get()) }
    factory { ChartScreenModel(get()) }
}

fun main() = application {

    startKoin {
        modules(mainModule)
    }

    val state = rememberWindowState(
        width = WINDOW_INITIAL_WIDTH.dp,
        height = WINDOW_INITIAL_HEIGHT.dp,
        position = WindowPosition(Alignment.Center)
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "Expense Tracker",
        state = state,
        resizable = true
    ) {
        window.minimumSize = Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT)
        AppTheme() {
            Navigator(HomeScreen())
        }
    }
}

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val spendingScreenModel = getScreenModel<SpendingScreenModel>()
        val categoryScreenModel = getScreenModel<CategoryScreenModel>()
        val limitScreenModel = getScreenModel<LimitScreenModel>()
        val chartScreenModel = getScreenModel<ChartScreenModel>()
        var currentDestination by remember { mutableStateOf(Destination.SPENDINGS) }

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
                            SpendingScreen(spendingScreenModel, modifier = Modifier.fillMaxSize())
                        }

                        Destination.CATEGORIES -> {
                            KoinTestComposable(
                                categoryScreenModel
                            )
                        }

                        Destination.LIMITS -> {
                            KoinTestComposable(
                                categoryScreenModel
                            )
                        }

                        Destination.CHARTS -> {
                            KoinTestComposable(
                                categoryScreenModel
                            )
                        }

                        Destination.SETTINGS -> {
                            Surface(modifier = Modifier.fillMaxSize()) { }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KoinTestComposable(
    model: CategoryScreenModel
) {
    val categories = model.categories.collectAsState(emptyList())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier.fillMaxSize()
            .padding(64.dp)
    ) {
        items(categories.value) { category ->
            Text(
                text = "$category",
                modifier = Modifier.padding(8.dp))
        }
    }

}