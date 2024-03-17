import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {

    fun createDriver(): SqlDriver?
}

@Composable
expect fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font