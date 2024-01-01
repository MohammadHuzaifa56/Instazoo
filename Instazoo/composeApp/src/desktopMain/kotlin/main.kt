import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import presentation.main.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Instazoo") {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}