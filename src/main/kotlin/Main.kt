import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.compose.AppTheme
import ui.HomeScreen
import java.awt.Dimension

// global constants to set initial main window size
const val WIDTH = 1280
const val HEIGHT = 720

fun main() = application {

    // state of main window size
    val state = rememberWindowState(
        width = WIDTH.dp,
        height = HEIGHT.dp,
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
            useDarkTheme = true
        ) {
            // composable function from which interface starts rendering
            HomeScreen()
        }
    }

}
