package ui.limitscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Category

@Composable
fun LimitDialog(
    onAddButtonClick: (Category, Double) -> Unit,
    onCloseDialog: () -> Unit,
    category: List<Category>
) {

    var amount by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("Wybierz kategorię") }
    var chosenCategory by remember { mutableStateOf(0) }

    var isError by remember { mutableStateOf(false) }

    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(48.dp)
                .fillMaxHeight()
                .wrapContentWidth()

        ) {
            Text(
                text = "Ustal limit",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = amount,
                isError = isError,
                label = { Text("Kwota") },
                onValueChange = { amount = it },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()
//                            .focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.height(36.dp))
            OutlinedTextField(
                value = categoryName,
                label = { Text("Kategria") },
                singleLine = true,
                readOnly = true,
                onValueChange = { },
                modifier = Modifier
                    .padding(12.dp)
                    .focusable()

//                            .focusRequester(focusRequester)
            )
            Button(
                onClick = {
                    isError = false
                    if(Validator.isValidAmount(amount)) {
                        onAddButtonClick(category[chosenCategory], amount.toDouble())
                        onCloseDialog()
                    } else isError = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Dodaj")
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(48.dp)
                .fillMaxHeight()
                .weight(1f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(200.dp)
            ) {
                items(category) {
                    Text(
                        text = it.name,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                chosenCategory = category.indexOf(it)
                                categoryName = it.name
                            }
                    )
                }
            }
        }
    }

}