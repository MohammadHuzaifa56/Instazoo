package presentation.home

import data.model.FeedPost
import data.model.StoryItem
import data.remote.Resource
import data.repository.HomeRepositoryImpl
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedPostsUIState(
    val isLoading: Boolean = false,
    val postsList: List<FeedPost>?= null,
    val error: String? = null
)

data class StoriesUIState(
    val isLoading: Boolean = false,
    val storiesList: List<StoryItem>?= null,
    val error: String? = null
)
class HomeScreenViewModel : ViewModel() {
    private val _postUiState = MutableStateFlow(FeedPostsUIState())
    val postsUiStateFlow = _postUiState.asStateFlow()

    private val homeRepository = HomeRepositoryImpl()

    private val _storyUiState = MutableStateFlow(StoriesUIState())
    val storyUIState = _storyUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val feedPosts = async { homeRepository.getFeedPosts() }
            val storyItems = async { homeRepository.getStories() }

            when (val result = feedPosts.await()) {
                is Resource.Loading -> _postUiState.update {
                    FeedPostsUIState(
                        isLoading = true,
                        postsList = null,
                        error = null
                    )
                }

                is Resource.Success -> _postUiState.update {
                    FeedPostsUIState(
                        isLoading = false,
                        postsList = result.data ?: emptyList(),
                        error = null,
                    )
                }

                is Resource.Error -> _postUiState.update {
                    FeedPostsUIState(
                        isLoading = false,
                        postsList = null,
                        error = result.message,
                    )
                }
            }

            when (val result = storyItems.await()) {
                is Resource.Loading -> _storyUiState.update {
                    StoriesUIState(
                        isLoading = true,
                        storiesList = null,
                        error = null
                    )
                }

                is Resource.Success -> _storyUiState.update {
                    StoriesUIState(
                        isLoading = false,
                        storiesList = result.data ?: emptyList(),
                        error = null,
                    )
                }

                is Resource.Error -> _storyUiState.update {
                    StoriesUIState(
                        isLoading = false,
                        storiesList = null,
                        error = result.message,
                    )
                }
            }
        }
    }
}