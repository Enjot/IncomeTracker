package ui.leftcontent

import SpendingItem
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LeftContent(
    onAddClick: () -> Unit,
    items: List<SpendingItem>,
    modifier: Modifier = Modifier
) {

    // column to store elements
    Column(
        modifier
            .padding(24.dp)
    ) {

        Month()
        Row(
            modifier = Modifier
                .align(Alignment.End)
        ) {
            // TODO zrobić filtrowanie po kategoriach
            SortingButton(
                inputValue = "Filtruj",
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))

            SortingButton(
                inputValue = "Od najnowszych",
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )
        }


        ListOfSpending(
            onAddClick = onAddClick,
            items,
            modifier = Modifier
                .padding(12.dp)
        )

    }
}
