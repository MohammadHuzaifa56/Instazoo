package presentation.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun Modifier.shimmerEffect(toShow:Boolean): Modifier = composed {

    if (toShow){
        var size by remember {
            mutableStateOf(IntSize.Zero)
        }
        val transition = rememberInfiniteTransition(label = "")
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(1000)
            ), label = ""
        )
        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB8B5B5),
                    Color(0xFF8F8B8B),
                    Color(0xFFB8B5B5),
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        )
            .onGloballyPositioned {
                size = it.size
            }
    }else{
        Modifier
    }
}

@Composable
fun InstaLoadingProgress(progress: Float) {
    Box(modifier = Modifier.size(50.dp).drawWithContent {
        drawRoundRect(
            color = Color.LightGray,
            cornerRadius = CornerRadius(size.width, size.width),
            style = Stroke(width = 1.dp.toPx())
        )
        if (progress>0) {
            drawProgressDownloadCurve(progress = progress)
        }
        drawContent()
    })
}

fun ContentDrawScope.drawProgressDownloadCurve(progress: Float){
    drawArc(
        color = Color.White,
        startAngle = -90f,
        sweepAngle = progress,
        useCenter = false,
        size = Size(size.width, size.height),
        style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
    )
    val center = Offset(size.width / 2f, size.height / 2f)
    val beta = (progress - 90f) * (PI / 180f).toFloat()
    val r = size.width / 2f
    val a = cos(beta) * r
    val b = sin(beta) * r
    drawPoints(
        listOf(Offset(center.x + a, center.y + b)),
        pointMode = PointMode.Points,
        color = Color.White,
        strokeWidth = 10.dp.toPx(),
        cap = StrokeCap.Round
    )
}