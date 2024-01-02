package data.remote

import data.model.FeedPost
import data.model.StoryItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val BASE_URL = "https://mohammadhuzaifa56.github.io/TestInstaAPI/"

class InstazooAPI: KoinComponent {

    private val client: HttpClient by inject()
    suspend fun fetchFeedPosts(baseUrl: String = BASE_URL, endPoint: String): List<FeedPost> =
        client.get("$baseUrl$endPoint").body<List<FeedPost>>()

    suspend fun fetchStories(baseUrl: String = BASE_URL, endPoint: String): List<StoryItem> =
        client.get("$baseUrl$endPoint").body<List<StoryItem>>()
}