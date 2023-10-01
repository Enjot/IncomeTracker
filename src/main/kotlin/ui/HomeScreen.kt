package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import ui.leftcontent.LeftContent
import ui.rightcontent.RightContent

// root composable function

class HomeScreen : Screen {
    @Composable
    fun HomeScreen(modifier: Modifier = Modifier) {

        val screenModel = rememberScreenModel { HomeScreenModel() }
        // blank surface that fill whole window and change color itself depending on theme
        Surface(
            color = MaterialTheme.colors.surface,
            modifier = modifier
        ) {
            // inside main window we made two separated blocks to handle interface
            Row() {
                LeftContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
                RightContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }
    }
}

