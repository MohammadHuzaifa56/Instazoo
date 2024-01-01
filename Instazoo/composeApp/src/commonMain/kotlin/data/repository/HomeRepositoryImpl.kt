package data.repository

import data.model.FeedPost
import data.model.StoryItem
import data.remote.InstazooAPI
import data.remote.Resource
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException

class HomeRepositoryImpl: HomeRepository {
    override suspend fun getFeedPosts(): Resource<List<FeedPost>> {
        return try {
            val response = InstazooAPI.fetchFeedPosts(endPoint = "feedPosts.json")
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

    override suspend fun getStories(): Resource<List<StoryItem>> {
        return try {
            val response = InstazooAPI.fetchStories(endPoint = "storyPosts.json")
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