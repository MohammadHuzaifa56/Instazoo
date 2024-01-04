package presentation.main

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import di.appModule
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        MaterialTheme {
            TabNavigator(HomeTab) {
                Scaffold(bottomBar = {
                    BottomNavigation(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.LightGray
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(SearchTab)
                        TabNavigationItem(AddTab)
                        TabNavigationItem(ReelsTab)
                        TabNavigationItem(ProfileTab)
                    }
                }) {
                    Box(Modifier.padding(it)){
                        CurrentTab()
                    }
                }
            }
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