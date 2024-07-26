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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import data.model.FeedPost
import data.model.UserStory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.stories.StoriesPagerScreen
import presentation.utils.InstaLoadingProgress


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun HomeScreen(homeViewModel: HomeScreenViewModel = koinInject()) {
    val feedPostsUIState by homeViewModel.postsUiStateFlow.collectAsState()
    val storiesUIState by homeViewModel.storyUIState.collectAsState()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isExpanded = calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Expanded

    Scaffold(backgroundColor = MaterialTheme.colors.background, topBar = {
        if (isExpanded.not()) {
            TopAppBar(
                title = {
                    Text("Instazoo", fontSize = 22.sp, color = MaterialTheme.colors.primary)
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.background,
                    scrolledContainerColor = MaterialTheme.colors.background
                ), scrollBehavior = scrollBehavior
            )
        }
    }, modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)) {
        ModalBottomSheetLayout(
            sheetContent = {
                CommentsSheetView(homeViewModel)
            },
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally)
            {
                item {
                    storiesUIState.mainStoriesList?.let {
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
fun StoryItemView(storyItem: UserStory?) {
    val navigator = LocalNavigator.currentOrThrow
    val tabNavigator = LocalTabNavigator.current
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
                    ).clickable {
                        navigator.push(StoriesPagerScreen(userId = storyItem?.userId?.toInt() ?: 0))
                    }
            )
            KamelImage(
                resource = asyncPainterResource(storyItem?.profilePic.orEmpty()),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp).clip(CircleShape)
            )
        }
        Text(
            text = storyItem?.userName.orEmpty(),
            modifier = Modifier.width(70.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ImageItem(postItem: FeedPost, openComments: () -> Unit) {
    var isLiked by remember {
        mutableStateOf(false)
    }

    val likeColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else MaterialTheme.colors.primary
    )

    val likeIcon by remember {
        derivedStateOf {
            if (isLiked) "ic_heart_filled.xml" else "ic_heart_like.xml"
        }
    }

    val isExpanded = calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Expanded

    Column(modifier = Modifier.padding(6.dp).then(if (isExpanded) Modifier.width(500.dp) else Modifier)) {
        Box {
            KamelImage(
                resource = asyncPainterResource(postItem.postImage.orEmpty()),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                onLoading = { progress ->
                    InstaLoadingProgress(progress = progress)
                },

                modifier = Modifier.fillMaxWidth().aspectRatio(0.8f)
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

            Spacer(Modifier.width(8.dp))
            Icon(
                painter = painterResource("ic_share.xml"),
                contentDescription = "",
                modifier = Modifier.size(22.dp).padding(2.dp)
            )
        }
        Text(
            text = "${postItem.likes} likes",
            modifier = Modifier.run { padding(4.dp) },
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}