package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryItem(
    @SerialName("user_name")
    var userName: String? = null,

    @SerialName("profile_pic")
    var profilePic: String? = null,
)

fun orgsampleinstazoodb.StoryItemEntity.toDomain(): StoryItem {
    return StoryItem(userName = user_name, profilePic = user_profile_pic)
}

fun StoryItem.toData(): orgsampleinstazoodb.StoryItemEntity {
    return orgsampleinstazoodb.StoryItemEntity(this.userName, this.profilePic)
}