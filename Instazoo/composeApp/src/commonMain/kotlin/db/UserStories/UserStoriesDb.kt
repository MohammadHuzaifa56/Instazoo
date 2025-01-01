package db.UserStories

import data.model.UserStory
import data.model.toDomain
import org.sample.instazoo.db.InstaZooDatabase

class UserStoriesDb(private val db: InstaZooDatabase?) {
//    fun getAllStories(): List<UserStory>? =
//        db?.storyPostsQueries?.selectAllStories()
//            ?.executeAsList()?.map {
//                it.toDomain()
//            }
}