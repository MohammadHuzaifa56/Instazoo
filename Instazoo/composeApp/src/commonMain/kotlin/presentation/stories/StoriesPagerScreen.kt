package presentation.stories

import InstaPrimaryColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import data.model.StoryItem
import data.model.UserStory
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.utils.InstaLoadingProgress
import kotlin.math.absoluteValue
import kotlin.math.min

class StoriesPagerScreen(val userId: Int) : Screen {
    @Composable
    override fun Content() {
        StoriesMainScreen(userId)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun StoriesMainScreen(userId: Int, storiesMainViewModel: StoriesMainViewModel = koinInject()) {
    val storiesListState by storiesMainViewModel.userStoriesUIState.collectAsState()
    var currentPagingIndex by remember { mutableStateOf(1) }
    val currentStoryIndex by remember { mutableStateOf(0) }
    val navigator = LocalNavigator.current
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    val pagerState = rememberPagerState(
        initialPageOffsetFraction = 0f,
    ) {
        storiesListState.mainStoriesList?.size ?: 0
    }

    LaunchedEffect(storiesListState) {
        currentPagingIndex =
            storiesListState.mainStoriesList?.indexOfFirst { it?.userId == userId.toLong() } ?: 0
        pagerState.scrollToPage(currentPagingIndex)
    }

    var currentPage by remember {
        mutableStateOf(currentStoryIndex)
    }

    val scope = rememberCoroutineScope()
    var pauseStory by remember {
        mutableStateOf(false)
    }
    var pauseTimer by remember {
        mutableStateOf(0L)
    }

    val currentUserStoriesList = remember {
        mutableStateListOf<StoryItem>()
    }

    val currentStoryItem by remember {
        derivedStateOf { currentUserStoriesList.getOrNull(currentPage) }
    }

    val storiesPagerState = rememberPagerState(
        initialPage = currentPage
    ) {
        currentUserStoriesList.size
    }

    val scale by remember {
        derivedStateOf {
            1f - (pagerState.currentPageOffsetFraction.absoluteValue) * .3f
        }
    }

    var currentUserStoryMain by remember {
        mutableStateOf(storiesListState.mainStoriesList?.get(currentPagingIndex))
    }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect {
            currentPagingIndex = it
            currentUserStoriesList.clear()
            currentUserStoryMain = storiesListState.mainStoriesList?.get(currentPagingIndex)
            currentUserStoriesList.addAll(
                currentUserStoryMain?.storiesList
                    ?: emptyList()
            )
            currentPage = 0
            storiesPagerState.scrollToPage(currentPage)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        val isCompact = widthSizeClass == WindowWidthSizeClass.Compact

        if (isCompact.not()) {
            storiesListState.mainStoriesList?.let {
                LazyColumn(
                    modifier = Modifier.then(
                        if (widthSizeClass == WindowWidthSizeClass.Medium) Modifier.weight(
                            0.2f
                        ) else Modifier.weight(0.3f)
                    ).background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(it) {
                        StoryNavItemView(
                            userStory = it,
                            isSelected = it?.userId == currentUserStoryMain?.userId,
                            isExpanded = widthSizeClass == WindowWidthSizeClass.Expanded
                        ) { selectedId ->
                            currentPagingIndex =
                                storiesListState.mainStoriesList?.indexOfFirst { it?.userId == selectedId }
                                    ?: 0
                            scope.launch {
                                pagerState.scrollToPage(currentPagingIndex)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.weight(0.1f))
        }

        Box(
            modifier = Modifier
                .background(Color.Black)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val offsetFromStart = pagerState.offsetForPage(0).absoluteValue
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .offset { IntOffset(0, 150.dp.roundToPx()) }
                    .scale(scaleX = .6f, scaleY = .24f)
                    .scale(scale)
                    .rotate(offsetFromStart * 90f)
                    .blur(
                        radius = 110.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded,
                    )
                    .background(Color.Black.copy(alpha = .5f))
            )

            HorizontalPager(
                pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                try {
                                    pauseStory = true
                                    delay(100)
                                    pauseTimer = pauseTimer.plus(100)
                                    awaitRelease()
                                } finally {
                                    pauseStory = false
                                    pauseTimer = 0
                                }
                            },
                            onDoubleTap = {

                            },
                            onTap = {
                                if (it.x > 500) {
                                    scope.launch {
                                        if (storiesPagerState.currentPage.plus(1) < currentUserStoriesList.size) {
                                            currentPage++
                                            storiesPagerState.animateScrollToPage(storiesPagerState.currentPage + 1)
                                        } else if (storiesPagerState.currentPage.plus(1) < pagerState.pageCount) {
                                            currentPage = 0
                                            pagerState.animateScrollToPage(currentPagingIndex + 1)
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        if (storiesPagerState.currentPage - 1 >= 0) {
                                            currentPage--
                                            storiesPagerState.animateScrollToPage(currentPage)
                                        } else if (pagerState.currentPage - 1 > 0) {
                                            currentPage =
                                                storiesListState.mainStoriesList?.get(
                                                    currentPagingIndex - 1
                                                )?.storiesList?.size
                                                    ?: 0
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                }
                            },
                            onLongPress = {
                            }
                        )
                    }
            ) { pagePosition ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(
                            animationSpec = tween(
                                500,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        .graphicsLayer {
                            val pageOffset = pagerState.offsetForPage(pagePosition)
                            val offScreenRight = pageOffset < 0f
                            val deg = 105f
                            val interpolated =
                                FastOutLinearInEasing.transform(pageOffset.absoluteValue)
                            rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

                            transformOrigin = TransformOrigin(
                                pivotFractionX = if (offScreenRight) 0f else 1f,
                                pivotFractionY = .5f
                            )
                        }
                        .drawWithContent {
                            val pageOffset = pagerState.offsetForPage(pagePosition)

                            this.drawContent()
                            drawRect(
                                Color.Black.copy(
                                    (pageOffset.absoluteValue * .7f)
                                )
                            )
                        }
                        .background(Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.DarkGray)
                    ) {
                        var storyDownloaded by remember {
                            mutableStateOf(false)
                        }

                        HorizontalPager(
                            state = storiesPagerState,
                            userScrollEnabled = false,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            StoryImageView(storyItem = currentStoryItem, onLoading = {
                                storyDownloaded = false
                            }) {
                                storyDownloaded = true
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.8f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                ListOfIndicators(
                                    numberOfPages = currentUserStoriesList.size,
                                    pauseStory = pauseStory,
                                    onEveryStoryChange = {
                                        scope.launch {
                                            currentPage = it
                                            storiesPagerState.scrollToPage(it)
                                        }
                                    },
                                    changedPage = currentPage,
                                    storyDownloaded = storyDownloaded,
                                    onComplete = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(currentPagingIndex + 1)
                                        }
                                    }
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    navigator?.pop()
                                }) {
                                    Icon(
                                        painter = painterResource("ic_back.xml"),
                                        modifier = Modifier.size(22.dp),
                                        tint = Color.White,
                                        contentDescription = ""
                                    )
                                }

                                currentUserStoryMain?.profilePic?.let { asyncPainterResource(it) }
                                    ?.let {
                                        KamelImage(
                                            resource = it,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                Spacer(Modifier.width(8.dp))
                                currentUserStoryMain?.userName?.let {
                                    Text(text = it, color = Color.White, fontSize = 12.sp)
                                }
                                Spacer(Modifier.width(8.dp))
                                currentStoryItem?.time?.let {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                    StoryReplyView()
                }
            }
        }

        if (widthSizeClass == WindowWidthSizeClass.Expanded) {
            Spacer(Modifier.weight(0.1f))
        }

    }
}

@Composable
fun LinearIndicator(
    modifier: Modifier,
    onPauseTimer: Boolean = false,
    hideIndicators: Boolean = false,
    currentPage: Int = 0,
    index: Int = 0,
    storyDownloaded: Boolean = false,
    onAnimationEnd: () -> Unit
) {

    val updatedCurrentPage = rememberUpdatedState(currentPage)
    val updatedIndex = rememberUpdatedState(index)

    var progress by remember {
        mutableStateOf(0f)
    }

    val isDownloaded = rememberUpdatedState(storyDownloaded)

    val startProgress by remember {
        derivedStateOf {
            (updatedIndex.value == updatedCurrentPage.value) && isDownloaded.value
        }
    }

    val animatedProgress by animateFloatAsState(targetValue = progress)

    LaunchedEffect(key1 = currentPage) {
        if (updatedCurrentPage.value > updatedIndex.value) {
            progress = 1f
        } else if (updatedCurrentPage.value < updatedIndex.value) {
            progress = 0f
        } else if (updatedCurrentPage.value == updatedIndex.value) {
            progress = 0f
        }
    }

    if (startProgress) {
        LaunchedEffect(key1 = onPauseTimer) {
            while (progress < 1f && isActive && onPauseTimer.not() && updatedCurrentPage.value == updatedIndex.value) {
                progress += 0.01f
                delay(30)
            }

            //When the timer is not paused and animation completes then move to next page.
            if (onPauseTimer.not()) {
                delay(200)
                onAnimationEnd()
            }
        }
    }

    if (hideIndicators.not()) {
        LinearProgressIndicator(
            trackColor = Color.White.copy(alpha = 0.3f),
            color = Color.White,
            modifier = modifier
                .padding(top = 12.dp, bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp)),
            progress = animatedProgress
        )
    }
}

@Composable
private fun RowScope.ListOfIndicators(
    numberOfPages: Int,
    pauseStory: Boolean,
    changedPage: Int,
    storyDownloaded: Boolean,
    onEveryStoryChange: ((Int) -> Unit)? = null,
    onComplete: () -> Unit,
) {

    val changePage = rememberUpdatedState(changedPage)

    var currentPage by remember(changedPage) {
        mutableStateOf(changePage.value)
    }

    val isDownloaded = rememberUpdatedState(storyDownloaded)

    val scope = rememberCoroutineScope()

    for (index in 0 until numberOfPages) {
        LinearIndicator(
            modifier = Modifier.weight(1f),
            onPauseTimer = pauseStory,
            currentPage = currentPage,
            index = index,
            storyDownloaded = isDownloaded.value,
            onAnimationEnd = {
                scope.launch {
                    currentPage++
                    if (currentPage < numberOfPages) {
                        onEveryStoryChange?.invoke(currentPage)
                    }

                    if (currentPage == numberOfPages) {
                        onComplete()
                    }
                }
            })
    }
}

@Stable
@Composable
fun StoryImageView(
    modifier: Modifier = Modifier,
    storyItem: StoryItem?,
    onLoading: () -> Unit,
    onComplete: () -> Unit
) {
    val painterResource = asyncPainterResource(
        storyItem?.sourceUrl.orEmpty(),
        filterQuality = FilterQuality.None,
        key = storyItem?.storyId
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        KamelImage(
            resource = painterResource,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            onLoading = { progress ->
                InstaLoadingProgress(progress = progress)
            }
        )
    }

    when (painterResource) {
        is Resource.Failure -> {}
        is Resource.Loading -> {
            onLoading()
        }

        is Resource.Success -> {
            onComplete()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StoryReplyView() {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = "Search",
            onValueChange = {},
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(fontSize = 14.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Icon(
            painter = painterResource("ic_heart_like.xml"),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Icon(
            painter = painterResource("ic_share.xml"),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun StoryNavItemView(userStory: UserStory?, isSelected: Boolean, isExpanded: Boolean, onUserStorySelect: (Long) -> Unit) {
    val selected = rememberUpdatedState(isSelected)
    val selectedColor by animateColorAsState(if (selected.value) InstaPrimaryColor else Color.Transparent)
    Row(
        Modifier.fillMaxSize().background(selectedColor).padding(8.dp).clickable {
            userStory?.userId?.let { onUserStorySelect.invoke(it) }
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KamelImage(
            resource = asyncPainterResource(userStory?.profilePic.orEmpty()),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(20.dp).size(60.dp).clip(CircleShape)
        )


        if (isExpanded) {
            Spacer(Modifier.width(8.dp))
            Text(text = userStory?.userName.orEmpty(), color = Color.White)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction