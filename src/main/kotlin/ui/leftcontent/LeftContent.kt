package ui.leftcontent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LeftContent(
    modifier: Modifier = Modifier
) {

    // column to store elements
    Column(
        modifier
            .padding(24.dp)
    ) {

        Month()

        SortingButton(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .align(Alignment.End)
        )

        ListOfSpending(
            listOfSpendingItems,
            modifier = Modifier
                .padding(12.dp)
        )

    }
}
