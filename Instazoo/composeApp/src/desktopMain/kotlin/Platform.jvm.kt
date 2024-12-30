import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.ReelsItem

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun VideoPlayer(modifier: Modifier, reelsItem: ReelsItem, pagerState: PagerState,
                       pageIndex: Int,) {
    Column {
        reelsItem.sources?.let {
            VideoPlayerImpl(
                url = it,
                modifier = Modifier.fillMaxWidth().height(400.dp)
            )
        }
    }
}

actual fun getVideoPath(name: String): String? {
    // Assuming video is in your app bundle
    return null
}