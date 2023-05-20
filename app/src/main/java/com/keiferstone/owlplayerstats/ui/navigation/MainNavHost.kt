package com.keiferstone.owlplayerstats.ui.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keiferstone.owlplayerstats.R
import com.keiferstone.owlplayerstats.ui.screen.PlayerComparisonScreen
import com.keiferstone.owlplayerstats.ui.screen.PlayerDetailScreen
import com.keiferstone.owlplayerstats.ui.screen.PlayerGridScreen
import com.keiferstone.owlplayerstats.ui.screen.StatListScreen
import com.keiferstone.owlplayerstats.ui.theme.AppTheme

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navItems = remember { navBarItems() }
    var selectedNavItem by remember { mutableStateOf(navItems.first()) }

    AppTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    navItems.forEach { navItem ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(navItem.iconResId),
                                    contentDescription = stringResource(navItem.nameResId))
                            },
                            label = {
                                Text(text = stringResource(navItem.nameResId))
                            },
                            selected = selectedNavItem == navItem,
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
                                selectedNavItem = navItem
                            }
                        )
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
                    StatListScreen()
                }
                composable(route = NavRoutes.PLAYER_GRID) {
                    PlayerGridScreen(
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
            }
        }
    }
}

fun navBarItems() = buildList {
    add(NavItem(R.string.stats, R.drawable.ic_rounded_outlined_leaderboard_24, NavRoutes.STAT_LIST))
    add(NavItem(R.string.players, R.drawable.ic_rounded_outlined_groups_24, NavRoutes.PLAYER_GRID))
}