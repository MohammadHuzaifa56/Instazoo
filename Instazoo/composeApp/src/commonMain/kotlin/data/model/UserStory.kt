package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import orgsampleinstazoodb.GetSingleUserStory
import orgsampleinstazoodb.SelectAllUserStories
import orgsampleinstazoodb.Story_items
import orgsampleinstazoodb.User_stories

@Serializable
data class UserStory(
    @SerialName("user_id")
    var userId: Long,

    @SerialName("user_name")
    var userName: String? = null,

    @SerialName("profile_pic")
    var profilePic: String? = null,

    @SerialName("stories")
    var storiesList: List<StoryItem>? = null,

    var isAllCaughtUp: Boolean? = false,

    var currentStoryPosition: Int? = 0
)

fun SelectAllUserStories.toUserStory(storyItems: List<Story_items>?): UserStory? {
    return storyItems?.map { it.toStoryItem() }?.let {
        UserStory(
            userId = user_id,
            userName = user_name,
            profilePic = user_profile_pic,
            storiesList = it
        )
    }
}

fun Story_items.toStoryItem(): StoryItem {
    return StoryItem(
        storyType = story_type,
        sourceUrl = url,
        storyId = story_id,
        time = time
    )
}

fun List<User_stories>.toUserStoryList(): List<UserStory> {
    return this.map {
        UserStory(
            userId = it.user_id,
            userName = it.user_name,
            storiesList = null,
            profilePic = it.user_profile_pic
        )
    }
}

fun UserStory.toUserStoriesEntity(): User_stories {
    return User_stories(
        user_id = userId,
        user_name = userName,
        user_profile_pic = profilePic
    )
}

fun StoryItem.toStoryItemsEntity(userId: Long): Story_items {
    return Story_items(
        story_id = storyId,
        user_id = userId,
        story_type = storyType,
        time = time,
        url = sourceUrl
    )
}