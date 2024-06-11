package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class StoryItem(

    @Transient
    var storyId: Long = 0,

    @SerialName("type")
    var storyType: String?,

    @SerialName("source_url")
    var sourceUrl: String?,

    @SerialName("time")
    var time: String?
)
