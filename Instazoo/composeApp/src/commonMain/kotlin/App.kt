import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable 
fun App() {
    MaterialTheme {
        Scaffold(topBar = {
            Row(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                Text("Instazoo", fontSize = 22.sp, color = Color.Black)
            }
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
                item {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(20){
                            StoryItem(
                                "Cristiano",
                                "https://i.pinimg.com/564x/e4/be/6e/e4be6e614d9c53add1100410379c93ae.jpg"
                            )
                            Spacer(Modifier.width(14.dp))
                        }
                    }
                }
                
                items(20) {
                    ImageItem("Cristiano",
                              "https://i.pinimg.com/564x/e4/be/6e/e4be6e614d9c53add1100410379c93ae.jpg",
                              "https://i.pinimg.com/564x/e4/be/6e/e4be6e614d9c53add1100410379c93ae.jpg")
                }
            }
        }
    }
}

@Composable
fun StoryItem(name: String, image: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        KamelImage(
            resource = asyncPainterResource(image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp).clip(CircleShape)
                .border(
                    BorderStroke(
                        2.dp,
                        Brush.linearGradient(listOf(Color.Yellow, Color.Red, Color.Blue, Color.Cyan))
                    )
                )
        )
        Text(text = name, color = Color.Black, fontSize = 12.sp)
    }
}

@Composable
fun ImageItem(name: String, profilePic: String, postUrl: String) {
    Box(Modifier.padding(10.dp).wrapContentSize()){
        KamelImage(
            resource = asyncPainterResource(profilePic),
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
}