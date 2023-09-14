package ui.leftcontent

import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// here we will execute sorting mechanism logic

@Composable
fun SortingButton(
    inputValue: String,
    modifier: Modifier = Modifier
) {

    var value by remember { mutableStateOf(inputValue) }

    OutlinedTextField(
        value = value,
        readOnly = true,
        onValueChange = { value = it },
        modifier = modifier
            .width(200.dp)
    )
}
