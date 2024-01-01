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
