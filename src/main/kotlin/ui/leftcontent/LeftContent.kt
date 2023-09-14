package ui.leftcontent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
