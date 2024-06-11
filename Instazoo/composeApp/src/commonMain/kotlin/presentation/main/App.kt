package presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import di.appModule
import org.koin.compose.KoinApplication
import org.koin.core.context.stopKoin

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        InstazooTheme {
            var isVisible by remember { mutableStateOf(true) }
            val homeTab = remember {
                HomeTab(
                    onNavigator = { isVisible = it }
                )
            }

            TabNavigator(homeTab) {
                Scaffold(bottomBar = {
                    AnimatedVisibility(visible = isVisible) {
                        BottomNavigation(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = MaterialTheme.colors.background
                        ) {
                            TabNavigationItem(homeTab)
                            TabNavigationItem(SearchTab)
                            TabNavigationItem(AddTab)
                            TabNavigationItem(ReelsTab)
                            TabNavigationItem(ProfileTab)
                        }
                    }
                }) {
                    Box(Modifier.padding(it)) {
                        CurrentTab()
                    }
                }
            }
        }
    }

    DisposableEffect(Unit){
        onDispose {
            stopKoin()
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {

    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab},
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        }
    )
}