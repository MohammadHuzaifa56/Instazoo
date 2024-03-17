import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun instaTypography(): Typography {
    val instaSansRegular = FontFamily(
        font(
            "InstaSans", "insta_sans_regular", FontWeight.Normal, FontStyle.Normal
        )
    )

    val instaSansSemiBold = FontFamily(
        font(
            "InstaSans", "insta_sans_medium", FontWeight.Normal, FontStyle.Normal
        )
    )
    val instaSansBold = FontFamily(
        font(
            "InstaSans", "insta_sans_bold", FontWeight.Normal, FontStyle.Normal
        )
    )

    return Typography(
        h1 = TextStyle(fontFamily = instaSansSemiBold),
        h2 = TextStyle(fontFamily = instaSansBold),
        body1 = TextStyle(fontFamily = instaSansRegular)
    )
}
