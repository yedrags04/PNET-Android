package com.heistcorp.heistcraft.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.heistcorp.heistcraft.navigation.AppNavHost
import com.heistcorp.heistcraft.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                scrollBehavior = topAppBarScrollBehavior,
                title = {
                    Text("HeistCraft")
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.QuestionAnswer,
                            contentDescription = "FAQ",
                        )
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                destinations = appState.topLevelDestinations,
                currentDestination = currentDestination,
                onDestinationClick = appState::navigateToTopLevelDestination
            )
        }
    ) { innerPadding ->
        AppNavHost(
            navController = appState.navController,
            startDestination = Destination.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun AppBottomBar(
    destinations: List<Destination>,
    currentDestination: NavDestination?,
    onDestinationClick: (Destination) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.secondary,
        contentColor = colorScheme.onSecondary,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isRouteInHierarchy(destination.route),
                onClick = { onDestinationClick(destination) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.onSecondary,
                    indicatorColor = colorScheme.surfaceVariant,
                    unselectedIconColor = colorScheme.onSurfaceVariant,
                    unselectedTextColor = colorScheme.onSurfaceVariant
                ),
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.contentDescription
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}
