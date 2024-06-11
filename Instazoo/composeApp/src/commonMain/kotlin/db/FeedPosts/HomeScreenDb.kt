package db.FeedPosts

import data.model.FeedPost
import data.model.UserStory
import data.model.toData
import data.model.toDomain
import data.model.toStoryItemsEntity
import data.model.toUserStoriesEntity
import data.model.toUserStory
import data.model.toUserStoryList
import org.sample.instazoo.db.InstaZooDatabase
import orgsampleinstazoodb.GetSingleUserStory
import orgsampleinstazoodb.User_stories

class HomeScreenDb(private val database: InstaZooDatabase?) {
    fun getAllFeedPosts(): List<FeedPost>? =
        database?.feedPostsQueries?.selectAllFeedPosts()
            ?.executeAsList()?.map {
                it.toDomain()
            }


    fun insertFeedPosts(feedPostsList: List<FeedPost>) {
        database?.transaction {
            feedPostsList.forEach {
                database.feedPostsQueries.insertFeedPost(it.toData())
            }
        }
    }

    fun getAllStories(): List<UserStory?>?{
        val userStoryEntities = database?.storyPostsQueries?.selectAllUserStories()?.executeAsList()
        val userStories = userStoryEntities?.map { userStoryEntity ->
            val storyItemsEntities = database?.storyPostsQueries?.getSingleUserStories(userStoryEntity.user_id)?.executeAsList()
            userStoryEntity.toUserStory(storyItemsEntities)
        }
        return userStories
    }

    fun getMainStories(): List<UserStory>?{
        val userStoryEntities = database?.storyPostsQueries?.getMainStoriesList()?.executeAsList()
        return userStoryEntities?.toUserStoryList()
    }

    fun getSingleUserStory(userId: Int): List<GetSingleUserStory>? {
        return database?.storyPostsQueries?.getSingleUserStory(user_id = userId.toLong())
            ?.executeAsList()
    }


    fun insertStories(storiesList: List<UserStory>) {
        database?.transaction {
            val userStoriesEntities = storiesList.map { it.toUserStoriesEntity() }

            storiesList.forEach {
                database.storyPostsQueries.insertUserStories(it.userId, it.userName, it.profilePic)
            }

            val allStoryItemsEntities = storiesList.flatMap { story ->
                story.storiesList?.let { list ->
                    list.map { it.toStoryItemsEntity(story.userId) }
                } ?: emptyList()
            }

            allStoryItemsEntities.forEach {
                database.storyPostsQueries.insertStoryItem(null, it.user_id, it.story_type, it.url, it.time)
            }
        }
    }
}