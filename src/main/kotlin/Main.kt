
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
import com.example.compose.AppTheme
import org.koin.core.context.startKoin
import ui.HomeScreen
import ui.HomeScreenModel
import java.awt.Dimension

fun main() = application {
    
    
    
    startKoin {
        modules(homeModule)
    }
    
    // state of main window size
    val state = rememberWindowState(
        width = WINDOW_INITIAL_WIDTH.dp,
        height = WINDOW_INITIAL_HEIGHT.dp,
        position = WindowPosition(Alignment.Center)
    )

    // initialize main window
    Window(
        onCloseRequest = ::exitApplication,
        title = "Income Tracker",
        state = state,
        resizable = true
    ) {

        
        // set minimum window size
        window.minimumSize = Dimension(1280,720)


        // AppTheme allows to use custom Material Theme color set from theme package
        /* TODO change code logic so we can make a button, that switch between light and dark theme */
        AppTheme(
            // if you use
            // useDarkTheme = isSystemInDarkTheme()
            // app will follow system theme at launch, but it won't update theme if you change system theme
            // after changing system theme in settings, you will have to relaunch app to apply changes
        ) {
            // composable function from which interface starts rendering
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

