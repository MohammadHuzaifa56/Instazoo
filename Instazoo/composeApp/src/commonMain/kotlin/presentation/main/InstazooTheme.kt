package presentation.main

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import instaTypography


@Composable
fun InstazooTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        content = content,
        typography = instaTypography()
    )
}