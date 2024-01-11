package presentation.search

import data.model.SearchItem
import data.remote.Resource
import data.repository.search.SearchRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

data class SearchPostsUIState(
    val isLoading: Boolean = false,
    val searchPostsList: List<SearchItem>?= null,
    val error: String? = null
)

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel(), KoinComponent{
    private val _searchPostUiState = MutableStateFlow(SearchPostsUIState())
    val searchPostUiState = _searchPostUiState.asStateFlow()

    init {
        getSearchPosts()
    }

    private fun getSearchPosts() {
        viewModelScope.launch {
            _searchPostUiState.update { SearchPostsUIState(isLoading = true) }
            searchRepository.getFeedPosts().collect { resource ->
                when (resource) {
                    is Resource.Success -> _searchPostUiState.update {
                        SearchPostsUIState(
                            searchPostsList = resource.data,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> _searchPostUiState.update {
                        SearchPostsUIState(
                            error = resource.message,
                            isLoading = false
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}