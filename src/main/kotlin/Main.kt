import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
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
import ui.categoryscreen.CategoryScreen
import ui.categoryscreen.CategoryScreenModel
import ui.chartscreen.ChartScreen
import ui.chartscreen.ChartScreenModel
import ui.limitscreen.LimitScreen
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

fun main()  {

    startKoin {
        modules(mainModule)
    }

    application {
        val state = rememberWindowState(
            width = WINDOW_INITIAL_WIDTH.dp, height = WINDOW_INITIAL_HEIGHT.dp, position = WindowPosition(Alignment.Center)
        )
        Window(
            onCloseRequest = ::exitApplication, title = "Expense Tracker", state = state, resizable = true
        ) {
            window.minimumSize = Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT)
            AppTheme {
                Navigator(HomeScreen())
            }
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
                    modifier = Modifier.width(100.dp).fillMaxHeight()
                ) {
                    SingleNavigationRailItem(
                        selected = currentDestination == Destination.SPENDINGS,
                        onClick = { currentDestination = Destination.SPENDINGS },
                        text = "Wydatki",
                        selectedIcon = painterResource("drawable/icons/spendingFilled.svg"),
                        unselectedIcon = painterResource("drawable/icons/spending.svg")
                    )
                    SingleNavigationRailItem(
                        selected = currentDestination == Destination.CATEGORIES,
                        onClick = { currentDestination = Destination.CATEGORIES },
                        text = "Kategorie",
                        selectedIcon = painterResource("drawable/icons/categoryFilled.svg"),
                        unselectedIcon = painterResource("drawable/icons/category.svg")
                    )
                    SingleNavigationRailItem(
                        selected = currentDestination == Destination.LIMITS,
                        onClick = { currentDestination = Destination.LIMITS },
                        text = "Limity",
                        selectedIcon = painterResource("drawable/icons/limitsFilled.svg"),
                        unselectedIcon = painterResource("drawable/icons/limits.svg")
                    )
                    SingleNavigationRailItem(
                        selected = currentDestination == Destination.CHARTS,
                        onClick = { currentDestination = Destination.CHARTS },
                        text = "Statystyki",
                        selectedIcon = painterResource("drawable/icons/chartsFilled.svg"),
                        unselectedIcon = painterResource("drawable/icons/charts.svg")
                    )
                    SingleNavigationRailItem(
                        selected = currentDestination == Destination.SETTINGS,
                        onClick = { currentDestination = Destination.SETTINGS },
                        text = "Ustawienia",
                        selectedIcon = painterResource("drawable/icons/settingsFilled.svg"),
                        unselectedIcon = painterResource("drawable/icons/settings.svg")
                    )
                }
                AnimatedContent(
                    targetState = currentDestination,
                    modifier = Modifier
                        .shadow(
                            128.dp,
                            shape = RoundedCornerShape(topStart = 64.dp, bottomStart = 64.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface)
                ) { destination ->
                    when (destination) {
                        Destination.SPENDINGS -> {
                            SpendingScreen(spendingScreenModel)
                        }

                        Destination.CATEGORIES -> {
                            CategoryScreen(categoryScreenModel)
                        }

                        Destination.LIMITS -> {
                            LimitScreen(limitScreenModel)
                        }

                        Destination.CHARTS -> {
                            ChartScreen(chartScreenModel)
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    selectedIcon: Painter,
    unselectedIcon: Painter,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(true) }
    val alpha: Float by animateFloatAsState(if (pressed) 1f else 0.9f)

    NavigationRailItem(selected = selected,
        onClick = onClick,
        label = { Text(text) },
        icon = {
            Icon(
                if (selected) {
                    selectedIcon
                } else {
                    unselectedIcon
                },
                contentDescription = null,
            )
        },
        colors = NavigationRailItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier.padding(vertical = 12.dp).graphicsLayer(scaleX = alpha, scaleY = alpha)
            .onPointerEvent(PointerEventType.Press) { pressed = false }
            .onPointerEvent(PointerEventType.Release) { pressed = true }
    )
}


@Composable
fun KoinTestComposable(
    model: CategoryScreenModel
) {
    val categories = model.categories.collectAsState(emptyList())
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp), modifier = Modifier.fillMaxSize().padding(64.dp)
    ) {
        items(categories.value) { category ->
            Text(
                text = "$category", modifier = Modifier.padding(8.dp)
            )
        }
    }

}

enum class Destination {
    SPENDINGS,CATEGORIES, LIMITS, CHARTS, SETTINGS
}

