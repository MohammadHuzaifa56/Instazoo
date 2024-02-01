package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentItem(
    @SerialName("comment") val comment: String,
    @SerialName("likes") val likes: Int,
    @SerialName("profile_name") val profileName: String,
    @SerialName("profile_pic") val profilePic: String,
    @SerialName("time_duration") val timeDuration: String
)