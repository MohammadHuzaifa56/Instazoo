package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReelsItem(
    @SerialName("description")
    val description: String = "",
    @SerialName("sources")
    val sources: String?,
    @SerialName("subtitle")
    val subtitle: String?,
    @SerialName("thumb")
    val thumb: String?,
    @SerialName("title")
    val title: String?
)

fun orgsampleinstazoodb.ReelsItemEntity.toDomain(): ReelsItem {
    return ReelsItem(
        sources = this.sources,
        subtitle = this.subtitle,
        thumb = this.thumb,
        title = this.title
    )
}

fun ReelsItem.toData(): orgsampleinstazoodb.ReelsItemEntity {
    return orgsampleinstazoodb.ReelsItemEntity(
        sources = this.sources,
        subtitle = this.subtitle,
        thumb = this.thumb,
        title = this.title
    )
}