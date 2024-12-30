package presentation.reels

import data.model.ReelsItem
import data.remote.Resource
import data.repository.reels.ReelsRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

data class ReelsUIState(
    val isLoading: Boolean = false,
    val reelsList: List<ReelsItem>? = null,
    val error: String? = null
)

class ReelsViewModel(private val reelsRepository: ReelsRepository) : ViewModel(), KoinComponent {

    private val _reelsUiState = MutableStateFlow(ReelsUIState())
    val reelsUiState = _reelsUiState.asStateFlow()

    init {
        getReelsData()
    }

    private fun getReelsData() {
        viewModelScope.launch {
            _reelsUiState.update { ReelsUIState(isLoading = true) }
            reelsRepository.getReelsData().collect { resource ->
                when (resource) {
                    is Resource.Success -> _reelsUiState.update {
                        ReelsUIState(
                            reelsList = resource.data, isLoading = false
                        )
                    }

                    is Resource.Error -> _reelsUiState.update {
                        ReelsUIState(
                            error = resource.message, isLoading = false
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}
