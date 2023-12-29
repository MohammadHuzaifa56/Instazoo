import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.resource

@Composable 
fun App() {
    MaterialTheme {
        Scaffold(topBar = {
            Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(12.dp)) {
                Text("Instazoo", fontSize = 22.sp, color = Color.Black)
            }
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
                item {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(20) {
                            StoryItem(
                                "Cristiano",
                                "https://i.pinimg.com/564x/e4/be/6e/e4be6e614d9c53add1100410379c93ae.jpg"
                            )
                            Spacer(Modifier.width(14.dp))
                        }
                    }
                }

                items(20) {
                    ImageItem(
                        "Cristiano",
                        "https://i.pinimg.com/564x/e4/be/6e/e4be6e614d9c53add1100410379c93ae.jpg",
                        "https://media.gettyimages.com/id/1325105287/photo/portugal-v-france-uefa-euro-2020-group-f.jpg?s=1024x1024&w=gi&k=20&c=QcO3g94yyTv9lf-Q4UXPsoJT5_E2tcKPtI8y0olpwLg="
                    )
                }
            }
        }
    }
}

@Composable
fun StoryItem(name: String, image: String) {
    val linearEasingAngle by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1440f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 8000,
                easing = LinearEasing,
                delayMillis = 100
            ),
            repeatMode = RepeatMode.Restart
        )
    )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center){
                Box(Modifier.size(63.dp).clip(CircleShape).graphicsLayer { rotationZ = linearEasingAngle }.border(
                    BorderStroke(
                        width = 2.dp,
                        brush = Brush.linearGradient(listOf(Color.Yellow, Color.Red, Color.Blue, Color.Cyan))),
                    shape = CircleShape
                ))
                KamelImage(
                    resource = asyncPainterResource(image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp).clip(CircleShape)

                )
            }
            Text(text = name, color = Color.Black, fontSize = 12.sp)
        }
    }

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ImageItem(name: String, profilePic: String, postUrl: String) {
    Column {
        Box(Modifier.padding(10.dp).wrapContentSize()) {
            KamelImage(
                resource = asyncPainterResource(postUrl),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
            )

            Row(modifier = Modifier.padding(10.dp).align(Alignment.TopStart)) {
                KamelImage(
                    resource = asyncPainterResource(profilePic),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(30.dp).clip(CircleShape)
                )
                Text(text = name, fontSize = 12.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row {
            Icon(painter = painterResource("ic_heart.xml"), contentDescription = "", modifier = Modifier.size(34.dp))
            Text(text = "32", fontSize = 14.sp)

            Icon(painter = painterResource("ic_heart.xml"), contentDescription = "", modifier = Modifier.size(34.dp))
            Text(text = "32", fontSize = 14.sp)

            Icon(painter = painterResource("ic_heart.xml"), contentDescription = "", modifier = Modifier.size(34.dp))
            Text(text = "32", fontSize = 14.sp)
        }
    }
}