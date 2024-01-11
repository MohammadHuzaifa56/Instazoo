package db.FeedPosts

import data.model.FeedPost
import data.model.StoryItem
import data.model.toData
import data.model.toDomain
import org.sample.instazoo.db.InstaZooDatabase

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

    fun getAllStories(): List<StoryItem>? =
        database?.storyPostsQueries?.selectAllStories()
            ?.executeAsList()?.map {
                it.toDomain()
            }


    fun insertStories(storiesList: List<StoryItem>) {
        database?.transaction {
            storiesList.forEach {
                database.storyPostsQueries.insertStoryItem(it.toData())
            }
        }
    }
}