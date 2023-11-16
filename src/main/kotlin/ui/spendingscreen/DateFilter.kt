package ui.spendingscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateFilterSelector(
    monthsNames: List<String>,
    selectedMonth: Int,
    selectedYear: Int,
    onYearSelect: (String) -> Unit,
    onMonthSelect: (String) -> Unit,
) {
    var month by remember { mutableStateOf(selectedMonth.toString()) }
    var year by remember { mutableStateOf(selectedYear.toString()) }
    
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = month,
            onValueChange = {
                month = it
                try {
                    onMonthSelect(it)
                } catch (_: Throwable) {

                }
            },
            label = {Text("Miesiąc")}
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = year,
            onValueChange = {
                year = it
                try {
                    onYearSelect(it)
                } catch (_: Throwable) {
                    
                }
            },
            label = {Text("Rok")}
        )
    }

}

