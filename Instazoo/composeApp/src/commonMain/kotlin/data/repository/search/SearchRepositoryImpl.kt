package data.repository.search

import data.model.SearchItem
import data.remote.InstazooAPI
import data.remote.Resource
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException

class SearchRepositoryImpl(private val api: InstazooAPI) : SearchRepository {
    override suspend fun getFeedPosts(): Resource<List<SearchItem>> {
        return try {
            val response = api.fetchSearchPosts(endPoint = "searchPosts.json")
            Resource.Success(response)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load"
            )
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
            Resource.Error(
                message = "time out"
            )
        }
    }
}