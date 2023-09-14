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
        Icon(
            painter = painterResource("drawable/picture_as_pdf_FILL1_wght400_GRAD0_opsz24.svg"),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(48.dp)
                .clickable{

                }
        )
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
