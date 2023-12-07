package ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Scrollbar(
    visible: Boolean,
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        VerticalScrollbar(
            adapter = adapter,
            style = defaultScrollbarStyle().copy(
                unhoverColor = MaterialTheme.colorScheme.outlineVariant,
                hoverColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .wrapContentHeight()
        )
    }
}

