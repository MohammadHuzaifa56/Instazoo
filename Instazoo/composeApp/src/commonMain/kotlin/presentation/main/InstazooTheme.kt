package presentation.main

import InstaThemeColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import instaTypography


private val DarkColorPalette = darkColors(
    primary = InstaThemeColors.Night.primaryTextColor,
    onPrimary = InstaThemeColors.Night.secondaryTextColor,
    surface = InstaThemeColors.Night.main_surface,
    background = InstaThemeColors.Night.main_background,
    onSecondary = InstaThemeColors.Night.secondary_surface
)

private val LightColorPalette = lightColors(
    primary = InstaThemeColors.Day.primaryTextColor,
    onPrimary = InstaThemeColors.Day.secondaryTextColor,
    surface = InstaThemeColors.Day.main_surface,
    background = InstaThemeColors.Day.main_background,
    onSecondary = InstaThemeColors.Day.secondary_surface
)
@Composable
fun InstazooTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }

    MaterialTheme(
        colors = LightColorPalette,
        content = content,
        typography = instaTypography()
    )
}