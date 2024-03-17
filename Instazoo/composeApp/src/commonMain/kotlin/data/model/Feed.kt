package data.model

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val likes: Int,
    val image_url: String
)