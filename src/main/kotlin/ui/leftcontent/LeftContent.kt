package ui.leftcontent

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Category
import com.example.sqldelight.Spending
import ui.HomeScreenModel

@Composable
fun LeftContent(
    screenModel: HomeScreenModel,
    onAddClick: (String, Double, Category) -> Unit,
    items: List<Spending>,
    onDeleteClick: (Long) -> Unit,
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
            screenModel = screenModel,
            onAddClick = onAddClick,
            onDeleteClick = onDeleteClick,
            list = items,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        )

    }
}
