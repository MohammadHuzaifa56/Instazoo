package data.repository.search

import data.model.SearchItem
import data.remote.InstazooAPI
import data.remote.Resource
import db.Database
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val api: InstazooAPI, private val database: Database) : SearchRepository {
    override suspend fun getFeedPosts(): Flow<Resource<List<SearchItem>>> = flow {
        try {
            val dbData = database.getAllSearchPosts()
            if (dbData?.isNotEmpty() == true){
                emit(Resource.Success(dbData))
            }else{
                val response = api.fetchSearchPosts(endPoint = "searchPosts.json")
                database.insertSearchPosts(response)
                emit(Resource.Success(response))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error(message = "Couldn't load"))
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
            emit(Resource.Error(message = "time out"))
        }
    }
}