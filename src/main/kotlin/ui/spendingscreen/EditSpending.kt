package ui.spendingscreen

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
import com.example.sqldelight.Spending

@Composable
fun EditSpending(
    onEditClick: (Long, String, Double) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onCloseDialog: () -> Unit,
    item: Spending,
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf(item.name) }
    var amount by remember { mutableStateOf(item.amount.toString()) }
    var isAmountValid by remember { mutableStateOf(true) }
    var isNameValid by remember { mutableStateOf(true) }

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
                text = "Edytuj Wydatek",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = name,
                label = { Text("Nazwa") },
                onValueChange = { name = it },
                isError = !isNameValid,
                supportingText = {if (!isNameValid) {
                    Row{
                        Icon(
                            painterResource("drawable/icons/error.svg"),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Nieprawidłowa nazwa",
                            fontSize = 16.sp
                        )
                    }
                } else null},
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
                        onDeleteClick(item.id)
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
                        when {
                            Validator.isEmptyString(name) -> isNameValid = true
                            !Validator.isEmptyString(name) -> isNameValid = false
                        }
                        if (isNameValid && isAmountValid) {
                            onEditClick(item.id, name, amount.toDouble())
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

