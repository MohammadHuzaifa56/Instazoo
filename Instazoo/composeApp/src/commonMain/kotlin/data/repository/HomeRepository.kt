package data.repository

import data.model.CommentItem
import data.model.FeedPost
import data.model.UserStory
import data.remote.Resource
import kotlinx.coroutines.flow.Flow
import orgsampleinstazoodb.GetSingleUserStory
import orgsampleinstazoodb.User_stories

interface HomeRepository {
    suspend fun getFeedPosts(): Flow<Resource<List<FeedPost>>>
    suspend fun getStories(): Flow<Resource<List<UserStory?>>>
    suspend fun getMainStoriesList(): Flow<Resource<List<UserStory>>>
    suspend fun getSingleStory(userId: Int): Flow<Resource<List<GetSingleUserStory>?>>
    suspend fun getComments(): Flow<Resource<List<CommentItem>>>
}