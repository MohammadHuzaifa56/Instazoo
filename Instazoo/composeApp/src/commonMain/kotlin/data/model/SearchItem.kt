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

fun orgsampleinstazoodb.SearchItem.toDomain(): SearchItem {
    return SearchItem(postImage = this.post_image, isSmall = this.is_small?.toInt() == 0)
}

fun SearchItem.toData(): orgsampleinstazoodb.SearchItem {
    return orgsampleinstazoodb.SearchItem(this.postImage, if (this.isSmall == true) 0 else 1)
}