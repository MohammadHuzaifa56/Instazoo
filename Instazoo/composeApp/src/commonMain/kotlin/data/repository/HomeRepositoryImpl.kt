package data.repository

import data.model.FeedPost
import data.model.StoryItem
import data.remote.InstazooAPI
import data.remote.Resource
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.utils.io.errors.IOException
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeRepositoryImpl: HomeRepository, KoinComponent {
    private val api: InstazooAPI by inject()
    override suspend fun getFeedPosts(): Resource<List<FeedPost>> {
        return try {
            val response = api.fetchFeedPosts(endPoint = "feedPosts.json")
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
            val response = api.fetchStories(endPoint = "storyPosts.json")
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