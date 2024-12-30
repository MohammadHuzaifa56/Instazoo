package data.repository.reels

import data.model.ReelsItem
import data.remote.InstazooAPI
import data.remote.Resource
import db.ReelsData.ReelsDataDb
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReelsRepositoryImpl(private val api: InstazooAPI, private val database: ReelsDataDb) :
    ReelsRepository {

    override suspend fun getReelsData(): Flow<Resource<List<ReelsItem>>> = flow {
        try {
            val dbData = database.getAllReels()
            if (dbData?.isNotEmpty() == true) {
                emit(Resource.Success(dbData))
            } else {
                val response = api.fetchReelsData(endPoint = "reels_data.json")
                database.insertReels(response)
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