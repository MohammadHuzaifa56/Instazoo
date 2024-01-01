package data.repository

import data.model.FeedPost
import data.model.StoryItem
import data.remote.Resource

interface HomeRepository {
    suspend fun getFeedPosts(): Resource<List<FeedPost>>
    suspend fun getStories(): Resource<List<StoryItem>>
}