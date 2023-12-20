package ui.limitscreen

import data.ViewModels.CurrentLimit
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditLimit(
    onEditClick: (String, Double, String) -> Unit,
    onDeleteClick: (String, String) -> Unit,
    onCloseDialog: () -> Unit,
    item: CurrentLimit,
    modifier: Modifier = Modifier
) {

    var amount by remember { mutableStateOf(item.limitAmount.toString()) }
    var isAmountValid by remember { mutableStateOf(true) }

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
                isError = !isAmountValid,
                supportingText = {if (!isAmountValid) {
                    Row{
                        Icon(
                            painterResource("drawable/icons/error.svg"),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Nieprawidłowa kwota",
                            fontSize = 16.sp
                        )
                    }
                } else null},
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
                        when {
                            Validator.isValidAmount(amount) -> isAmountValid = true
                            !Validator.isValidAmount(amount) -> isAmountValid = false
                        }
                        if (isAmountValid) {
                            onEditClick(item.categoryName,amount.toDouble(), item.currentDate)
                            run { onCloseDialog() }
                        }
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

