package ui.leftcontent

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

// here we will execute month handle logic
@Composable
fun Month(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Wrzesień",
        style = MaterialTheme.typography.displayMedium,
        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .clickable {

            }
    )
    Text(
        text = "kliknij aby zmienić miesiąc",
        style = MaterialTheme.typography.displayMedium
    )
}