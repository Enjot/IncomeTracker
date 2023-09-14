package ui.rightcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleLimitCard(
    item: LimitItem,
    actualProgress: Float,
    modifier: Modifier = Modifier
) {
    var isInEdit by remember { mutableStateOf(false) }
    var limitName by remember { mutableStateOf(item.limitName) }
    var currentExpenses by remember { mutableStateOf(item.currentExpenses.toString()) }
    var plannedExpenses by remember { mutableStateOf(item.plannedExpenses.toString()) }

    Card(
        onClick = {isInEdit = !isInEdit},
        shape = RoundedCornerShape(28.dp),
        elevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Row (
                modifier = Modifier
                    .align(Alignment.Start)
            ){
                if (isInEdit){
                    TextField(
                        value = limitName,
                        onValueChange = {limitName = it},
                        singleLine = true,
                    )
                }else{
                    Text(limitName)
                }


            }
            Spacer(modifier = Modifier.height(12.dp))
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                if (isInEdit){
                    TextField(
                        value = currentExpenses,
                        onValueChange = {currentExpenses = it},
                        singleLine = true,
                        modifier = Modifier
                            .width(50.dp)
                    )
                }else{
                    Text(currentExpenses)
                }
                LinearProgressIndicator(
                    progress = actualProgress,
                    backgroundColor = Color.LightGray,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .height(8.dp)
                        .align(Alignment.Bottom)
                )
                if (isInEdit){
                    TextField(
                        value = plannedExpenses,
                        onValueChange = {plannedExpenses = it},
                        singleLine = true,
                        modifier = Modifier
                            .width(62.dp)
                    )
                }else{
                    Text(plannedExpenses)
                }
            }

        }
    }
}
