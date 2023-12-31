import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedPostsUIState(
    val postsList: List<FeedPost> = emptyList()
)

data class StoriesUIState(
    val storiesList: List<StoryItem> = emptyList()
)
class HomeScreenViewModel : ViewModel() {
    private val _postUiState = MutableStateFlow(FeedPostsUIState(emptyList()))
    val postsUiStateFlow = _postUiState.asStateFlow()

    private val _storyUiState = MutableStateFlow(StoriesUIState(emptyList()))
    val storyUIState = _storyUiState.asStateFlow()

    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    fun getFeedPosts() {
        viewModelScope.launch {
            val posts = httpClient
                .get("https://mohammadhuzaifa56.github.io/TestInstaAPI/feedPosts.json")
                .body<List<FeedPost>>()
            _postUiState.update { state ->
                state.copy(postsList = posts)
            }
        }
    }

    fun getStories() {
        viewModelScope.launch {
            val stories = httpClient
                .get("https://mohammadhuzaifa56.github.io/TestInstaAPI/storyPosts.json")
                .body<List<StoryItem>>()
            _storyUiState.update { state ->
                state.copy(storiesList = stories)
            }
        }
    }
}