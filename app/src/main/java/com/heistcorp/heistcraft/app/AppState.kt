package com.heistcorp.heistcraft.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.heistcorp.heistcraft.navigation.Destination

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState = remember(navController) { AppState(navController) }

@Stable
class AppState(
    val navController: NavHostController
) {
    val topLevelDestinations: List<Destination> = Destination.topLevelDestinations

    @Composable
    fun currentDestination(): NavDestination? {
        val backStackEntry = navController.currentBackStackEntryAsState()
        return backStackEntry.value?.destination
    }

    fun navigateToTopLevelDestination(destination: Destination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { destination ->
        destination.route == route
    } == true
}
