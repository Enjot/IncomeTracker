package ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddButton(
    text: String,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ExtendedFloatingActionButton(
        text = { Text(text) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        },
        onClick = onClick,
        expanded = expanded,
        modifier = modifier
    )
}
