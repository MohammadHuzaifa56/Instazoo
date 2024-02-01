package data.repository

import data.model.CommentItem
import data.model.FeedPost
import data.model.StoryItem
import data.remote.InstazooAPI
import data.remote.Resource
import db.FeedPosts.HomeScreenDb
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepositoryImpl(private val api: InstazooAPI, private val homeScreenDb: HomeScreenDb) :
    HomeRepository {
    override suspend fun getFeedPosts(): Flow<Resource<List<FeedPost>>> = flow {
        try {
            val dbData = homeScreenDb.getAllFeedPosts()
            if (dbData?.isNotEmpty() == true) {
                emit(Resource.Success(dbData))
            } else {
                val response = api.fetchFeedPosts(endPoint = "feedPosts.json")
                homeScreenDb.insertFeedPosts(response)
                emit(Resource.Success(response))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message = "Couldn't load"
                )
            )
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    message = "time out"
                )
            )
        }
    }

    override suspend fun getStories(): Flow<Resource<List<StoryItem>>> = flow {
        try {
            val dbData = homeScreenDb.getAllStories()
            if (dbData?.isNotEmpty() == true) {
                emit(Resource.Success(dbData))
            } else {
                val response = api.fetchStories(endPoint = "storyPosts.json")
                homeScreenDb.insertStories(response)
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

    override suspend fun getComments(): Flow<Resource<List<CommentItem>>> = flow {
        try {
            val response = api.fetchComments(endPoint = "post_comments.json")
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