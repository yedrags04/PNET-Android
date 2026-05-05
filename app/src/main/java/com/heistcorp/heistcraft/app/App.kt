package com.heistcorp.heistcraft.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import com.heistcorp.heistcraft.navigation.AppNavHost
import com.heistcorp.heistcraft.navigation.Destination

@Composable
fun PnetApp(modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination()

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isRouteInHierarchy(destination.route),
                onClick = { onDestinationClick(destination) },
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
