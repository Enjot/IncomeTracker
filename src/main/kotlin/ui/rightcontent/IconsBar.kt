package ui.rightcontent

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconsBar(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ){

        Icon(
            painter = painterResource("drawable/picture_as_pdf_FILL1_wght400_GRAD0_opsz24.svg"),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(32.dp)
                .clickable {  }
        )
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(32.dp)
                .clickable {  }
        )


    }
}



@Preview()
@Composable
private fun PreviewIconsBar() {
    IconsBar()
}