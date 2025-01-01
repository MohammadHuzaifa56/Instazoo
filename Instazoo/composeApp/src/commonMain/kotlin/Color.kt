import androidx.compose.ui.graphics.Color


val secondaryBlackColor = Color(0xFF202020)

val InstaPrimaryColor = Color(0xFFC13584)
sealed class InstaThemeColors(
    val main_background: Color,
    val main_surface: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color,
    val secondary_surface: Color
)  {
    object Night: InstaThemeColors(
        main_background = Color.Black,
        main_surface = Color.DarkGray,
        primaryTextColor = Color.White,
        secondaryTextColor = Color.LightGray,
        secondary_surface = secondaryBlackColor
    )
    object Day: InstaThemeColors(
        main_background = Color.White,
        main_surface = Color.LightGray,
        primaryTextColor = Color.Black,
        secondaryTextColor = Color.DarkGray,
        secondary_surface = Color.White
    )
}