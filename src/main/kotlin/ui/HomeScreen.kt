package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ui.leftcontent.LeftContent
import ui.rightcontent.RightContent

// root composable function

    @Composable
    fun HomeScreen(
        screenModel: HomeScreenModel,
        modifier: Modifier = Modifier
    ) {

        val spendings = screenModel.spendings.collectAsState(emptyList())

        // blank surface that fill whole window and change color itself depending on theme
        Surface(
            color = MaterialTheme.colors.surface,
            modifier = modifier
        ) {
            // inside main window we made two separated blocks to handle interface
            Row() {
                LeftContent(
                    onAddClick = { name, amount -> screenModel.insertSpending(name, amount) },
                    onDeleteClick = { id -> screenModel.deleteSpending(id) },
                    items = spendings.value,
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