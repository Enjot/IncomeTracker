package ui.categoryscreen


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CategorySortPicker(
    onSortClick: (CategorySortType) -> Unit,
    value: String,
    modifier: Modifier = Modifier
) {
    val width = 225.dp
    var expanded by remember { mutableStateOf(false) }
    val arrowOrientation: Float by animateFloatAsState(
        targetValue = if (expanded) 180F else 0F,
        animationSpec = spring(
            stiffness = 1000f
        )
    )

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            singleLine = true,
            readOnly = true,
            leadingIcon = {
                Icon(
                    painterResource("drawable/icons/sorting.svg"),
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(arrowOrientation)
                )
            },
            modifier = Modifier
                .width(width)

        )
        Spacer(
            modifier = Modifier
                .width(width)
                .height(56.dp)
                .clickable { expanded = !expanded }
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(width)
    ) {
        DropdownMenuItem(
            text = { Text(CategorySortType.NAME_INC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.NAME_INC)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(CategorySortType.NAME_DEC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.NAME_DEC)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(CategorySortType.SUM_INC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.SUM_INC)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(CategorySortType.SUM_DEC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.SUM_DEC)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(CategorySortType.AMOUNT_INC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.AMOUNT_INC)
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text(CategorySortType.AMOUNT_DEC.visibleName) },
            onClick = {
                onSortClick(CategorySortType.AMOUNT_DEC)
                expanded = false
            }
        )
    }
}