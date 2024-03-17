package data.repository.profile

import data.model.AccountDetail
import data.remote.InstazooAPI
import data.remote.Resource
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(private val api: InstazooAPI) : ProfileRepository {
    override suspend fun getAccountDetail(): Flow<Resource<AccountDetail>> = flow {
        try {
            val response = api.fetchAccountDetail(endPoint = "account_detail.json")
            emit(Resource.Success(response))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = "Couldn't load"))
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
            emit(Resource.Error(message = "time out"))
        }
    }

}