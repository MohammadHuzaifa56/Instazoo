package presentation.stories

import data.model.UserStory
import data.remote.Resource
import data.repository.HomeRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import orgsampleinstazoodb.GetSingleUserStory

data class StoriesUIState(
    val isLoading: Boolean = false,
    val mainStoriesList: List<UserStory?>?= null,
    val error: String? = null
)
data class SingleUserStoryUIState(
    val isLoading: Boolean = false,
    val userStory: List<GetSingleUserStory>?= null,
    val error: String? = null
)

class StoriesMainViewModel: ViewModel(), KoinComponent {
    private val homeRepository: HomeRepository by inject()

    private val _userStoriesUiState = MutableStateFlow(StoriesUIState())
    val userStoriesUIState = _userStoriesUiState.asStateFlow()

    private val _singleUserStoryUIState = MutableStateFlow(SingleUserStoryUIState())
    val singleUserStoryUIState = _singleUserStoryUIState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllUserStories()
        }
    }

    suspend fun getUserStory(userId: Int) {
        val response = homeRepository.getSingleStory(userId)
        response.collect { result ->
            when (result) {
                is Resource.Error -> _singleUserStoryUIState.update {
                    SingleUserStoryUIState(
                        isLoading = false,
                        userStory = null,
                        error = result.message
                    )
                }

                is Resource.Loading -> _singleUserStoryUIState.update {
                    SingleUserStoryUIState(
                        isLoading = true,
                        userStory = null,
                        error = null
                    )
                }

                is Resource.Success -> _singleUserStoryUIState.update {
                    SingleUserStoryUIState(
                        isLoading = false,
                        userStory = result.data,
                        error = null
                    )
                }
            }
        }
    }

    private suspend fun getAllUserStories() {
        val response = homeRepository.getStories()
        response.collect { result ->
            when (result) {
                is Resource.Error -> _userStoriesUiState.update {
                    StoriesUIState(
                        isLoading = false,
                        mainStoriesList = null,
                        error = result.message
                    )
                }

                is Resource.Loading -> _userStoriesUiState.update {
                    StoriesUIState(
                        isLoading = true,
                        mainStoriesList = null,
                        error = null
                    )
                }

                is Resource.Success -> _userStoriesUiState.update {
                    StoriesUIState(
                        isLoading = false,
                        mainStoriesList = result.data?.distinct(),
                        error = null
                    )
                }
            }
        }
    }
}