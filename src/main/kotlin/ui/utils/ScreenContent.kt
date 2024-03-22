package ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp)
//            .clip(RoundedCornerShape(topStart = 64.dp, bottomStart = 64.dp))
            .shadow(
                10.dp,
//                shape = RoundedCornerShape(topStart = 64.dp, bottomStart = 64.dp)
            )
            .padding(start = 10.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.padding(start = 36.dp),
            content = content
        )
    }
}