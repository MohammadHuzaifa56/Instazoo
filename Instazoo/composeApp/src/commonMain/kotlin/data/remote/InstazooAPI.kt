package data.remote

import data.model.FeedPost
import data.model.StoryItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL = "https://mohammadhuzaifa56.github.io/TestInstaAPI/"

object InstazooAPI {
    private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

    private val client: HttpClient = HttpClient{
        install(ContentNegotiation){
            json(nonStrictJson)
        }
    }

    suspend fun fetchFeedPosts(baseUrl: String = BASE_URL, endPoint: String): List<FeedPost> =
        client.get("$baseUrl$endPoint").body<List<FeedPost>>()

    suspend fun fetchStories(baseUrl: String = BASE_URL, endPoint: String): List<StoryItem> =
        client.get("$baseUrl$endPoint").body<List<StoryItem>>()
}