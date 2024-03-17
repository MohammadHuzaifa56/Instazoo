package presentation.profile

import data.model.AccountDetail
import data.model.FeedPost
import data.remote.Resource
import data.repository.profile.ProfileRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.search.SearchPostsUIState

data class ProfileUIState(
    val isLoading: Boolean = false,
    val accountDetail: AccountDetail? = null,
    val error: String? = null
)


class ProfileScreenViewModel : ViewModel(), KoinComponent {
    private val profileRepository: ProfileRepository by inject()
    private val _profileUIState = MutableStateFlow(ProfileUIState())
    val profileUIState = _profileUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _profileUIState.update { ProfileUIState(isLoading = true) }
            profileRepository.getAccountDetail().collect { resource ->
                when (resource) {
                    is Resource.Success -> _profileUIState.update {
                        ProfileUIState(
                            accountDetail = resource.data,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> _profileUIState.update {
                        ProfileUIState(
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