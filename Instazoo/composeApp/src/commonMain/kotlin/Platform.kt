import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.model.ReelsItem

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@OptIn(ExperimentalFoundationApi::class)
@Composable
expect fun VideoPlayer(modifier: Modifier, reelsItem: ReelsItem, pagerState: PagerState,
                       pageIndex: Int,)

expect fun getVideoPath(name: String): String?