package com.heistcorp.heistcraft.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.heistcorp.heistcraft.navigation.AppNavHost
import com.heistcorp.heistcraft.navigation.Destination

@Composable
fun App(modifier: Modifier = Modifier) {
    val appState = rememberAppState()
    val navController = appState.navController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentDestination = appState.currentDestination()
    val hideBottomBar =
        currentRoute == Destination.Login.route || currentRoute == Destination.Signup.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (!hideBottomBar) {
                AppBottomBar(
                    destinations = appState.bottomDestinations,
                    currentDestination = currentDestination,
                    onDestinationClick = appState::navigateToTopLevelDestination,
                )
            }
        },
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = Destination.Inicio.route,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun AppBottomBar(
    destinations: List<Destination>,
    currentDestination: NavDestination?,
    onDestinationClick: (Destination) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.secondary,
        contentColor = colorScheme.onSecondary,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination.isRouteInHierarchy(destination.route),
                onClick = { onDestinationClick(destination) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = colorScheme.primary,
                        selectedTextColor = colorScheme.onSecondary,
                        indicatorColor = colorScheme.surfaceVariant,
                        unselectedIconColor = colorScheme.onSurfaceVariant,
                        unselectedTextColor = colorScheme.onSurfaceVariant,
                    ),
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.contentDescription,
                    )
                },
                label = { Text(destination.label) },
            )
        }
    }
}
