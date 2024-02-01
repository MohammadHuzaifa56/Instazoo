package presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.FeedPost
import data.model.StoryItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeScreenViewModel = koinInject()) {
    val feedPostsUIState by homeViewModel.postsUiStateFlow.collectAsState()
    val storiesUIState by homeViewModel.storyUIState.collectAsState()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("Instazoo", fontSize = 22.sp, color = Color.Black)
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                scrolledContainerColor = Color.White
            ), scrollBehavior = scrollBehavior
        )
    }, modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)) {
        ModalBottomSheetLayout(
            sheetContent = {
               CommentsSheetView(homeViewModel)
            },
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize())
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
                        ) {
                            scope.launch {
                                sheetState.show()
                            }
                        }
                    }
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
        Text(
            text = storyItem.userName.orEmpty(),
            modifier = Modifier.width(70.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ImageItem(postItem: FeedPost, openComments: ()-> Unit) {
    var isLiked by remember {
        mutableStateOf(false)
    }

    val likeColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Black
    )

    val likeIcon by remember {
        derivedStateOf {
            if (isLiked) "ic_heart_filled.xml" else "ic_heart_like.xml"
        }
    }

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
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(likeIcon),
                contentDescription = "",
                tint = likeColor,
                modifier = Modifier.size(26.dp).padding(1.dp).clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isLiked = !isLiked
                }
            )
            Text(text = postItem.likes.toString(), fontSize = 14.sp)

            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource("ic_comments.xml"),
                contentDescription = "",
                modifier = Modifier.size(24.dp).padding(2.dp).clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    openComments.invoke()
                }
            )
            Text(text = postItem.comments.toString(), fontSize = 14.sp)

            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource("ic_share.xml"),
                contentDescription = "",
                modifier = Modifier.size(22.dp).padding(2.dp)
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