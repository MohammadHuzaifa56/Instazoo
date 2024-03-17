package data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountDetail(
    val bio: String,
    val followers: Int,
    val following: Int,
    val posts: Int,
    val profile_pic: String,
    val feeds: List<Feed>,
    val profile_name: String,
    val user_name: String
)