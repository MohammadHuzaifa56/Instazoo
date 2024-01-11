package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import orgsampleinstazoodb.FeedPostItemEntity

@Serializable
data class FeedPost(
    @SerialName("user_name") var userName: String? = null,
    @SerialName("user_profile_pic") var userProfilePic: String? = null,
    @SerialName("post_image") var postImage: String? = null,
    @SerialName("location") var location: String? = null,
    @SerialName("likes") var likes: Int? = null,
    @SerialName("comments") var comments: Int? = null,
    @SerialName("share") var share: Int? = null
)

fun FeedPostItemEntity.toDomain(): FeedPost {
    return FeedPost(
        userName = user_name,
        userProfilePic = user_profile_pic,
        postImage = post_image,
        location = location,
        likes = likes?.toInt(),
        comments = comments?.toInt(),
        share = share?.toInt()
    )
}

fun FeedPost.toData(): FeedPostItemEntity {
    return FeedPostItemEntity(
        user_name = userName,
        user_profile_pic = userProfilePic,
        post_image = postImage,
        location = location,
        likes = likes?.toLong(),
        comments = comments?.toLong(),
        share = share?.toLong()
    )
}
