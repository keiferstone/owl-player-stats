package com.keiferstone.owlplayerstats.ui.navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keiferstone.data.model.StatType
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.extension.parseFilterArgs
import com.keiferstone.owlplayerstats.ui.screen.FilterScreen
import com.keiferstone.owlplayerstats.ui.screen.PlayerComparisonScreen
import com.keiferstone.owlplayerstats.ui.screen.PlayerDetailScreen
import com.keiferstone.owlplayerstats.ui.screen.PlayerGridScreen
import com.keiferstone.owlplayerstats.ui.screen.StatDetailScreen
import com.keiferstone.owlplayerstats.ui.screen.StatListScreen
import com.keiferstone.owlplayerstats.ui.theme.AppTheme

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navItems = remember { navBarItems() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AppTheme {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = navItems.any { it.route == currentDestination?.route },
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    NavigationBar {
                        navItems.forEach { navItem ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(navItem.iconResId),
                                        contentDescription = stringResource(navItem.nameResId)
                                    )
                                },
                                label = {
                                    Text(text = stringResource(navItem.nameResId))
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                                onClick = {
                                    navController.navigate(navItem.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = NavRoutes.STAT_LIST
            ) {
                composable(route = NavRoutes.STAT_LIST) {
                    StatListScreen(
                        onMoreFiltersSelected = { selectedFilters ->
                            navController.navigate("${NavRoutes.FILTERS}?${NavArgs.FILTERS}=${selectedFilters.joinToString { it.id }}")
                        },
                        onStatSelected = { statType ->
                            navController.navigate("${NavRoutes.STAT_DETAIL}/${statType.name}")
                        }
                    )
                }
                composable(
                    route = "${NavRoutes.STAT_DETAIL}/{${NavArgs.STAT_TYPE}}",
                    arguments = listOf(navArgument(NavArgs.STAT_TYPE) { type = NavType.StringType })) { backStackEntry ->
                    backStackEntry.arguments?.getString(NavArgs.STAT_TYPE)?.let {
                        StatDetailScreen(StatType.valueOf(it))
                    }
                }
                composable(route = NavRoutes.PLAYER_GRID) {
                    PlayerGridScreen(
                        onMoreFiltersSelected = { selectedFilters ->
                            navController.navigate("${NavRoutes.FILTERS}?${NavArgs.FILTERS}=${selectedFilters.joinToString { it.id }}")
                        },
                        onPlayerSelected = { player ->
                            navController.navigate("${NavRoutes.PLAYER_DETAIL}/${player.id}")
                        },
                        onPlayerPairSelected = { first, second ->
                            navController.navigate("${NavRoutes.PLAYER_COMPARISON}/${first.id}/${second.id}")
                        }
                    )
                }
                composable(
                    route = "${NavRoutes.PLAYER_DETAIL}/{${NavArgs.PLAYER_ID}}",
                    arguments = listOf(navArgument(NavArgs.PLAYER_ID) { type = NavType.LongType })
                ) { backStackEntry ->
                    backStackEntry.arguments?.getLong(NavArgs.PLAYER_ID)?.let {
                        PlayerDetailScreen(it)
                    }
                }
                composable(
                    route = "${NavRoutes.PLAYER_COMPARISON}/{${NavArgs.PLAYER_ID}}/{${NavArgs.PLAYER_ID_2}}",
                    arguments = listOf(
                        navArgument(NavArgs.PLAYER_ID) { type = NavType.LongType },
                        navArgument(NavArgs.PLAYER_ID_2) { type = NavType.LongType })
                ) { backStackEntry ->
                    val player1Id = backStackEntry.arguments?.getLong(NavArgs.PLAYER_ID)
                    val player2Id = backStackEntry.arguments?.getLong(NavArgs.PLAYER_ID_2)
                    if (player1Id != null && player2Id != null) {
                        PlayerComparisonScreen(player1Id, player2Id)
                    }
                }
                composable(
                    route = "${NavRoutes.FILTERS}?${NavArgs.FILTERS}={${NavArgs.FILTERS}}",
                    arguments = listOf(navArgument(NavArgs.FILTERS) {
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val filters = backStackEntry.arguments
                        ?.getString(NavArgs.FILTERS)
                        .parseFilterArgs()
                    FilterScreen(navController, filters)
                }
            }
        }
    }
}

fun navBarItems() = buildList {
    add(NavItem(R.string.stats, R.drawable.ic_rounded_outlined_leaderboard_24, NavRoutes.STAT_LIST))
    add(NavItem(R.string.players, R.drawable.ic_rounded_outlined_groups_24, NavRoutes.PLAYER_GRID))
}