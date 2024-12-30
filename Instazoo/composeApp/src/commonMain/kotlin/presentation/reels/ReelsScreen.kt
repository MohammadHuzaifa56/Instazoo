package presentation.reels

import VideoPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.ReelsItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.utils.Space

@Composable
fun ReelsScreen(reelsViewModel: ReelsViewModel = koinInject()) {

    val reelsData by reelsViewModel.reelsUiState.collectAsState()

    reelsData.reelsList?.let { videoList ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            VerticalVideoPager(videos = videoList)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalVideoPager(
    modifier: Modifier = Modifier,
    videos: List<ReelsItem>,
    initPage: Int? = 0,
) {
    val pagerState = rememberPagerState(initialPage = initPage ?: 0, pageCount = {
        videos.size
    })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState, lowVelocityAnimationSpec = tween(
            easing = LinearEasing, durationMillis = 300
        )
    )

    VerticalPager(
        state = pagerState,
        flingBehavior = fling,
        beyondBoundsPageCount = 1,
        modifier = modifier
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            VideoPlayer(Modifier, videos[page], pagerState, page)
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    FooterUi(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )

                    SideItems(modifier = Modifier)
                }
                12.dp.Space()
            }
        }
    }
}

@Composable
fun FooterUi(modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Bottom) {

    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SideItems(modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource("ic_heart_like.xml"),
            contentDescription = "",
            modifier = Modifier.size(32.dp).padding(1.dp)
        )

        Text(
            text = "214k",
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 15.dp, top = 5.dp)
        )

        Icon(
            painter = painterResource("ic_comments.xml"),
            contentDescription = "",
            modifier = Modifier.size(26.dp).padding(1.dp)
        )

        Text(
            text = "20k",
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 15.dp, top = 5.dp)
        )

        Icon(
            painter = painterResource("ic_share.xml"),
            contentDescription = "",
            modifier = Modifier.size(26.dp).padding(1.dp)
        )

        Text(
            text = "10k",
            fontSize = 15.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 15.dp, top = 5.dp)
        )


    }
}

