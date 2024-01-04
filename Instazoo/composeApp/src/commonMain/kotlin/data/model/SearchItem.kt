package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    @SerialName("user_name") var userName: String? = null,
    @SerialName("user_profile_pic") var userProfilePic: String? = null,
    @SerialName("post_image") var postImage: String? = null,
    @SerialName("location") var location: String? = null,
    @SerialName("likes") var likes: Int? = null,
    @SerialName("is_small") var isSmall: Boolean? = false,
    @SerialName("comments") var comments: Int? = null,
    @SerialName("share") var share: Int? = null
)
