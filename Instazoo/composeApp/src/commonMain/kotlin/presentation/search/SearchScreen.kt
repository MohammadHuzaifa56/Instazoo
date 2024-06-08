package presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.model.SearchItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.koinInject
import presentation.utils.InstaLoadingProgress
import presentation.utils.shimmerEffect

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = koinInject()) {
    val searchPosts by searchViewModel.searchPostUiState.collectAsState()

    searchPosts.isLoading?.let {
        SearchItemShimmerView(it)
    }

    searchPosts.searchPostsList?.let {searchList->
        Column(modifier = Modifier.fillMaxSize()){
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), modifier = Modifier.fillMaxSize(),
                verticalItemSpacing = 1.dp, horizontalArrangement = Arrangement.spacedBy(1.dp),
                state = rememberLazyStaggeredGridState()){
                    items(searchList){
                        SearchItemView(it)
                    }
                }
            }
    }
}

@Composable
fun SearchItemView(searchItem: SearchItem) {
    KamelImage(
        resource = asyncPainterResource(searchItem.postImage.orEmpty()),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.width(170.dp)
            .height(if (searchItem.isSmall == true) 170.dp else 270.dp),
        onLoading = {
            InstaLoadingProgress(progress = it)
        }
    )
}

@Composable
fun SearchItemShimmerView(isLoading: Boolean) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(3), modifier = Modifier.fillMaxSize(),
        verticalItemSpacing = 1.dp, horizontalArrangement = Arrangement.spacedBy(1.dp),
        state = rememberLazyStaggeredGridState()){
            items(20){
                Spacer(
                    Modifier.width(170.dp)
                        .height(if (it % 3 == 0) 270.dp else 170.dp)
                        .shimmerEffect(isLoading))
            }
    }
}