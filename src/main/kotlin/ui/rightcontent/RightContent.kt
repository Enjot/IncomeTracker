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
        IconsBar(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        /* TODO button to change theme */
        PieChart(modifier = Modifier
            .padding(12.dp)
            .weight(2f)
            .fillMaxSize()
        )
        Spacer(modifier = Modifier.height(24.dp))
        LimitList(
            listOfLimitItem,
            modifier = Modifier
                .padding(12.dp)
                .weight(5f)
                .fillMaxSize()
        )
    }
}


