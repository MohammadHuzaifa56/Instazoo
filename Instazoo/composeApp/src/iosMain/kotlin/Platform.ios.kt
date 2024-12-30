import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import data.model.ReelsItem
import io.ktor.client.engine.darwin.*
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRect
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIView

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(modifier: Modifier, reelsItem: ReelsItem, pagerState: PagerState,
                       pageIndex: Int,) {
    val player = remember { AVPlayer(uRL = NSURL.URLWithString(reelsItem.sources)!!) }
    val playerLayer = remember { AVPlayerLayer() }
    val avPlayerViewController = remember { AVPlayerViewController() }
    avPlayerViewController.player = player
    avPlayerViewController.showsPlaybackControls = true

    playerLayer.player = player
    // Use a UIKitView to integrate with your existing UIKit views
    UIKitView(
        factory = {
            // Create a UIView to hold the AVPlayerLayer
            val playerContainer = UIView()
            playerContainer.addSubview(avPlayerViewController.view)
            // Return the playerContainer as the root UIView
            playerContainer
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            playerLayer.setFrame(rect)
            avPlayerViewController.view.layer.frame = rect
            CATransaction.commit()
        },
        update = { view ->
            player.play()
            avPlayerViewController.player!!.play()
        },
        modifier = modifier)
}

actual fun getVideoPath(name: String): String? {
    // Assuming video is in your app bundle
    return Bundle.main.path(forResource: name, ofType: null)
}