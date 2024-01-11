package presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.FeedPost
import data.model.StoryItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(homeViewModel: HomeScreenViewModel = koinInject()) {
    val feedPostsUIState by homeViewModel.postsUiStateFlow.collectAsState()
    val storiesUIState by homeViewModel.storyUIState.collectAsState()

    Scaffold(topBar = {
        Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(12.dp)) {
            Text("Instazoo", fontSize = 22.sp, color = Color.Black)
        }
    }) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 20.dp))
        {
            item {
                storiesUIState.storiesList?.let {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(it) {
                            StoryItemView(
                                it
                            )
                            Spacer(Modifier.width(14.dp))
                        }
                    }
                }
            }

            feedPostsUIState.postsList?.let {
                items(it) { item ->
                    ImageItem(
                        item
                    )
                }
            }
        }
    }
}


@Composable
fun StoryItemView(storyItem: StoryItem) {
    val linearEasingAngle by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1440f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 8000,
                easing = LinearEasing,
                delayMillis = 100
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(63.dp).clip(CircleShape)
                    .graphicsLayer { rotationZ = linearEasingAngle }.border(
                        BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color.Yellow,
                                    Color.Red,
                                    Color.Blue,
                                    Color.Cyan
                                )
                            )
                        ),
                        shape = CircleShape
                    )
            )
            KamelImage(
                resource = asyncPainterResource(storyItem.profilePic.orEmpty()),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(CircleShape)
            )
        }
        Text(text = storyItem.userName.orEmpty(), modifier = Modifier.width(70.dp), maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Black, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ImageItem(postItem: FeedPost) {
    var isLiked by remember {
        mutableStateOf(false)
    }

    val likeColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Black
    )
    Column(modifier = Modifier.padding(6.dp)) {
        Box {
            KamelImage(
                resource = asyncPainterResource(postItem.postImage.orEmpty()),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                onLoading = { progress ->
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
                },

                modifier = Modifier.fillMaxWidth().aspectRatio(0.7f)
            )

            Row(
                modifier = Modifier.padding(10.dp).align(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KamelImage(
                    resource = asyncPainterResource(postItem.userProfilePic.orEmpty()),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(30.dp).clip(CircleShape)
                )
                Spacer(Modifier.width(5.dp))
                Column {
                    Text(
                        text = postItem.userName.orEmpty(),
                        fontSize = 14.sp,
                        color = Color.White,
                        style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(text = postItem.location.orEmpty(), fontSize = 10.sp, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource("ic_heart_like.xml"),
                contentDescription = "",
                tint = likeColor,
                modifier = Modifier.size(26.dp).clickable {
                    isLiked = !isLiked
                }
            )
            Text(text = postItem.likes.toString(), fontSize = 14.sp)

            Spacer(Modifier.width(6.dp))
            Icon(
                painter = painterResource("ic_comment.xml"),
                contentDescription = "",
                modifier = Modifier.size(22.dp)
            )
            Text(text = postItem.comments.toString(), fontSize = 14.sp)

            Spacer(Modifier.width(6.dp))
            Icon(
                painter = painterResource("ic_send.xml"),
                contentDescription = "",
                modifier = Modifier.size(26.dp)
            )
            Text(text = postItem.share.toString(), fontSize = 14.sp)
        }
    }
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