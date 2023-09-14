package ui.rightcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


val shape = RoundedCornerShape(28.dp)

@Composable
fun RightContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(24.dp)
    ) {
        /* TODO button to change theme */
        PieChart(modifier = Modifier
            .weight(2f)
            .fillMaxSize()
        )
        Spacer(modifier = Modifier.height(48.dp))
        LimitList(
            listOfLimitItem,
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
        )
    }
}


