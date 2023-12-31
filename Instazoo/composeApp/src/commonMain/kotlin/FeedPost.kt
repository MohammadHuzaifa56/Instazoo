import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
