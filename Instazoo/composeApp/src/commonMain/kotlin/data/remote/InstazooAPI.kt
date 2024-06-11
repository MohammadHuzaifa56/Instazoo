package data.remote

import data.model.AccountDetail
import data.model.CommentItem
import data.model.FeedPost
import data.model.SearchItem
import data.model.UserStory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

const val BASE_URL = "https://mohammadhuzaifa56.github.io/TestInstaAPI/"

class InstazooAPI(private val client: HttpClient) {
    suspend fun fetchFeedPosts(baseUrl: String = BASE_URL, endPoint: String): List<FeedPost> =
        client.get("$baseUrl$endPoint").body<List<FeedPost>>()

    suspend fun fetchStories(baseUrl: String = BASE_URL, endPoint: String): List<UserStory> =
        client.get("$baseUrl$endPoint").body<List<UserStory>>()

    suspend fun fetchSearchPosts(baseUrl: String = BASE_URL, endPoint: String): List<SearchItem> =
        client.get("$baseUrl$endPoint").body()

    suspend fun fetchComments(baseUrl: String = BASE_URL, endPoint: String): List<CommentItem> =
        client.get("$baseUrl$endPoint").body<List<CommentItem>>()

    suspend fun fetchAccountDetail(baseUrl: String = BASE_URL, endPoint: String): AccountDetail =
        client.get("$baseUrl$endPoint").body<AccountDetail>()
}