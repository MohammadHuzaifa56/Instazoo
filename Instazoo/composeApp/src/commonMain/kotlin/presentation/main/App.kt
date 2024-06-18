package presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import di.appModule
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplication
import org.koin.core.context.stopKoin

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalResourceApi::class)
@Composable
fun App() {

    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
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

            val selectedTab = rememberSaveable {
                mutableIntStateOf(0)
            }

            TabNavigator(homeTab) {
                Scaffold(bottomBar = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
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
                    }


                }) {
                    Box(Modifier.padding(it)) {
                        CurrentTab()
                    }
                }
            }

            if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium) {
                        NavigationRail {
                            NavigationRailItem(
                                selected = selectedTab.value == 0,
                                icon = {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Home),
                                        contentDescription = ""
                                    )
                                },
                                label = {
                                    Text(text = "Home", fontSize = 14.sp)
                                },
                                selectedContentColor = Color.Red,
                                onClick = {
                                    selectedTab.value = 0
                                }
                            )

                            NavigationRailItem(
                                selected = selectedTab.value == 1,
                                icon = {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Search),
                                        contentDescription = ""
                                    )
                                },
                                label = {
                                    Text(text = "Search", fontSize = 14.sp)
                                },

                                selectedContentColor = Color.Red,
                                onClick = {
                                    selectedTab.value = 1
                                }
                            )

                            NavigationRailItem(
                                selected = selectedTab.value == 2,
                                icon = {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Add),
                                        contentDescription = ""
                                    )
                                },
                                label = {
                                    Text(text = "Add", fontSize = 14.sp)
                                },
                                selectedContentColor = Color.Red,
                                onClick = {
                                    selectedTab.value = 2
                                }
                            )

                            NavigationRailItem(
                                selected = selectedTab.value == 3,
                                icon = {
                                    Icon(
                                        painter = painterResource("ic_reels_new.xml"),
                                        contentDescription = ""
                                    )
                                },
                                label = {
                                    Text(text = "Reels", fontSize = 14.sp)
                                },
                                selectedContentColor = Color.Red,
                                onClick = {
                                    selectedTab.value = 3
                                }
                            )

                            NavigationRailItem(
                                selected = selectedTab.value == 4,

                                icon = {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.AccountCircle),
                                        contentDescription = ""
                                    )
                                },
                                label = {
                                    Text(text = "Profile", fontSize = 14.sp)
                                },
                                alwaysShowLabel = false,
                                selectedContentColor = Color.Red,
                                onClick = {
                                    selectedTab.value = 4
                                }
                            )
                        }
                    } else if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded){
                        Column(modifier = Modifier.weight(0.2f).fillMaxHeight().background(Color.Gray), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            NavigationExpandedItem(
                                isSelected = selectedTab.value == 0,
                                icon = Icons.Default.Home,
                                label = "Home"
                            ) {
                                selectedTab.value = 0
                            }

                            NavigationExpandedItem(
                                isSelected = selectedTab.value == 1,
                                icon = Icons.Default.Search,
                                label = "Search"
                            ) {
                                selectedTab.value = 1
                            }

                            NavigationExpandedItem(
                                isSelected = selectedTab.value == 2,
                                icon = Icons.Default.Add,
                                label = "Add"
                            ) {
                                selectedTab.value = 2
                            }

                            NavigationExpandedItem(
                                isSelected = selectedTab.value == 3,
                                icon = Icons.Default.PlayArrow,
                                label = "Reels"
                            ) {
                                selectedTab.value = 3
                            }

                            NavigationExpandedItem(
                                isSelected = selectedTab.value == 4,
                                icon = Icons.Default.AccountCircle,
                                label = "Profile"
                            ) {
                                selectedTab.value = 4
                            }
                        }
                    }

                    when (selectedTab.value) {
                        0 -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colors.background)
                            ) {
                                TabNavigator(homeTab)
                            }
                        }

                        1 -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colors.background)
                            ) {
                                TabNavigator(SearchTab)
                            }
                        }

                        2 -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colors.background)
                            ) {
                                TabNavigator(AddTab)
                            }
                        }

                        3 -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colors.background)
                            ) {
                                TabNavigator(ReelsTab)
                            }
                        }

                        4 -> {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colors.background)
                            ) {
                                TabNavigator(ProfileTab)
                            }
                        }
                    }
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                stopKoin()
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

@Composable
fun NavigationExpandedItem(
    isSelected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Red.copy(alpha = 0.5f) else Color.Transparent, animationSpec = tween(200, easing = LinearOutSlowInEasing))
    Row(Modifier.padding(12.dp).fillMaxWidth().wrapContentHeight().background(color = backgroundColor, shape = RoundedCornerShape(20.dp)).padding(8.dp).clickable { onClick.invoke() }, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )

        Spacer(Modifier.weight(0.1f))

        Text(text = label, fontSize = 14.sp)

        Spacer(Modifier.weight(1f))
    }
}