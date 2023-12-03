package ui.limitscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditLimit(
    onEditClick: (String, Double, String) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    onCloseDialog: () -> Unit,
    item: CurrentLimit,
    modifier: Modifier = Modifier
) {

    var amount by remember { mutableStateOf(item.limitAmount.toString()) }
    Box{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(48.dp)
                .wrapContentWidth()
        ) {
            Text(
                text = "Edytuj Limit",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = item.categoryName,
                readOnly = true,
                label = { Text("Kategoria") },
                onValueChange = {  },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = amount,
                label = { Text("Kwota") },
                onValueChange = { amount = it },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()
            )
            Row {
                Button(
                    onClick = {
                        onDeleteClick(item.categoryName, item.currentDate)
                        run { onCloseDialog() }
                    },
                ) {
                   Text("Usuń")
                }
                Spacer(modifier.width(24.dp))
                Button(
                    onClick = {
                        onEditClick(item.categoryName,amount.toDouble(), item.currentDate)
                        run { onCloseDialog() }
                    },
                ) {
                    Text("Edytuj")
                }
            }
            Spacer(modifier.width(24.dp))
            TextButton(
                onClick = { onCloseDialog() }
            ){
                Text("Anuluj")
            }

        }

    }
}

