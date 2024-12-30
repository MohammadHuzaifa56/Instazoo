import android.graphics.Bitmap
import android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import data.model.ReelsItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sample.instazoo.utils.FileUtils

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
actual fun VideoPlayer(
    modifier: Modifier, reelsItem: ReelsItem, pagerState: PagerState,
    pageIndex: Int,
) {
    val context = LocalContext.current
    var thumbnail by remember {
        mutableStateOf<Pair<String?, Boolean>>(Pair(null, true))  //bitmap, isShow
    }

    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                thumbnail = thumbnail.copy(first = reelsItem.thumb, second = thumbnail.second)
            }
        }
    }

    if (pagerState.settledPage == pageIndex) {
        val exoPlayer = remember(context) {
            ExoPlayer.Builder(context).build().apply {
                videoScalingMode = VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_ONE
                setMediaItem(MediaItem.fromUri(Uri.parse(reelsItem.sources)))
                playWhenReady = true
                prepare()
                addListener(object : Player.Listener {
                    override fun onRenderedFirstFrame() {
                        super.onRenderedFirstFrame()
                        thumbnail = thumbnail.copy(second = false)
                    }
                })
            }
        }

        val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(key1 = lifecycleOwner) {
            val lifeCycleObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        exoPlayer.pause()
                    }

                    Lifecycle.Event.ON_START -> exoPlayer.play()
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(lifeCycleObserver)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifeCycleObserver)
            }
        }

        val playerView = remember {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        DisposableEffect(key1 = AndroidView(factory = {
            playerView
        }), effect = {
            onDispose {
                thumbnail = thumbnail.copy(second = true)
                exoPlayer.release()
            }
        })
    }

    if (thumbnail.second) {
        KamelImage(
            resource = asyncPainterResource(thumbnail.first.orEmpty()),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }

}

actual fun getVideoPath(name: String): String? {
    return "asset:///videos/$name" // Assuming video is in assets folder
}