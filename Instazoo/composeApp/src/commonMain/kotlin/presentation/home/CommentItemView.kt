package presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.CommentItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CommentItemView(commentItem: CommentItem) {

    var isLiked by remember {
        mutableStateOf(false)
    }

    val likeColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.DarkGray
    )

    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        KamelImage(
            resource = asyncPainterResource(commentItem.profilePic),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(30.dp).clip(CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.wrapContentHeight().weight(1f)) {
            Text(
                text = commentItem.profileName,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = commentItem.comment,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 10.sp
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource("ic_heart_like.xml"),
                contentDescription = "",
                tint = likeColor,
                modifier = Modifier.size(18.dp).clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isLiked = !isLiked
                }
            )
            Modifier.height(2.dp)
            Text(text = commentItem.likes.toString(), color = Color.DarkGray, fontSize = 10.sp)
        }
    }
}