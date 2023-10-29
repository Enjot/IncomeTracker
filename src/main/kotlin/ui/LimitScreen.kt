package ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LimitScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        var expanded by remember { mutableStateOf(false) }

        val arrowOrientation: Float by animateFloatAsState(
            targetValue = if (expanded) 180F else 0F,
            animationSpec = spring(
                dampingRatio= Spring.DampingRatioMediumBouncy,
                stiffness = 800f
            )
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Box {
                OutlinedTextField(
                    value = "xD",
                    onValueChange = {  },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(arrowOrientation)
                        )
                    },
                    modifier = Modifier
                        .width(250.dp)
                        
                )
                Spacer(
                    modifier = Modifier
                        .width(250.dp)
                        .height(55.dp)
                        .clickable { expanded = !expanded }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = { /* Handle edit! */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .width(250.dp)
                )
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = { /* Handle settings! */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = null
                        )
                    })
                Divider()
                DropdownMenuItem(
                    text = { Text("Send Feedback") },
                    onClick = { /* Handle send feedback! */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = null
                        )
                    },
                    trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
            }
        }
    }
}