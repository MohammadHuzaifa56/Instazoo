package data.repository

import data.model.CommentItem
import data.model.FeedPost
import data.model.StoryItem
import data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getFeedPosts(): Flow<Resource<List<FeedPost>>>
    suspend fun getStories(): Flow<Resource<List<StoryItem>>>
    suspend fun getComments(): Flow<Resource<List<CommentItem>>>
}