package data.repository.profile

import data.model.AccountDetail
import data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getAccountDetail(): Flow<Resource<AccountDetail>>
}