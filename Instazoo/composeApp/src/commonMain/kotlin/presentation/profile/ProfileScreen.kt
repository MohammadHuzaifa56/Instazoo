package presentation.profile

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.AccountDetail
import data.model.Feed
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject
import presentation.utils.InstaLoadingProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileScreenViewModel: ProfileScreenViewModel = koinInject()) {
    val profileUIState by profileScreenViewModel.profileUIState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "smith_john",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colors.background,
                titleContentColor = MaterialTheme.colors.background
            ),
            actions = {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Menu),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            })

        profileUIState.accountDetail?.let {
            AccountDetailMainView(it)
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AccountDetailMainView(accountDetail: AccountDetail) {

    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact
    val itemsSpacing = if (isCompact) 1.dp else 8.dp
    val imageSize = if (isCompact) 80.dp else 150.dp

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        KamelImage(
            resource = asyncPainterResource(accountDetail.profile_pic),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(imageSize).clip(
                CircleShape
            )
        )


        if (isCompact.not()){
             BioInfo(modifier = Modifier.weight(2f), accountDetail)
        }

        Spacer(Modifier.weight(0.5f))
        VerticalDoubleText(accountDetail.posts.toString(), "Posts", modifier = Modifier.weight(1f))
        Spacer(Modifier.weight(0.2f))

        VerticalDoubleText(accountDetail.followers.toString(), "Followers", modifier = Modifier.weight(1f))
        Spacer(Modifier.weight(0.2f))

        VerticalDoubleText(accountDetail.following.toString(), "Following", modifier = Modifier.weight(1f))
        Spacer(Modifier.weight(0.2f))

    }

    if (isCompact) {
        BioInfo(accountDetail = accountDetail)
    }

    if (isCompact.not()) {
        Spacer(Modifier.height(10.dp))
    }
    Row(modifier = Modifier.padding(16.dp),horizontalArrangement = Arrangement.spacedBy(10.dp)) {

        if (isCompact.not()) {
            Spacer(Modifier.weight(2f))
        }

        androidx.compose.material3.Button(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colors.surface
        ), onClick = {}){
            Text(text = "Edit Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        androidx.compose.material3.Button(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colors.surface
        ), onClick = {}){
            Text(text = "Share Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(Modifier.height(20.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(itemsSpacing),
        horizontalArrangement = Arrangement.spacedBy(itemsSpacing),
        state = rememberLazyGridState()
    ) {
        items(accountDetail.feeds) {
            ImageItemView(it)
        }
    }
}

@Composable
fun BioInfo(modifier: Modifier = Modifier, accountDetail: AccountDetail) {
    Column(modifier = modifier) {
        Text(
            text = accountDetail.profile_name,
            modifier = Modifier.padding(start = 10.dp, top = 6.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = accountDetail.bio,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colors.onPrimary,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ImageItemView(feed: Feed) {
    KamelImage(
        resource = asyncPainterResource(feed.image_url),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.width(170.dp).aspectRatio(1f),
        onLoading = {
            InstaLoadingProgress(progress = it)
        }
    )
}

@Composable
fun VerticalDoubleText(firstText: String, secondText: String, modifier: Modifier) {

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = firstText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(text = secondText, fontSize = 14.sp)
    }
}
