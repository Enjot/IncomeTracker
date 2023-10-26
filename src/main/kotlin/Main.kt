import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import org.koin.core.context.startKoin
import ui.HomeScreen
import ui.HomeScreenModel
import ui.theme.AppTheme
import java.awt.Dimension

fun main() = application {
    startKoin {
        modules(homeModule)
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
        window.minimumSize = Dimension(900, 450)
        AppTheme() {
            Navigator(HomeScreen())
        }
    }
}

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        HomeScreen(screenModel)
    }
}

