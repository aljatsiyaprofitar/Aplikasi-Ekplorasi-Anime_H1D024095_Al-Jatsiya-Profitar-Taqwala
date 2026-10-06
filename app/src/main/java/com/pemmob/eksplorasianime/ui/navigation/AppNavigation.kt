package com.pemmob.eksplorasianime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.eksplorasianime.ui.screens.DetailScreen
import com.pemmob.eksplorasianime.ui.screens.HomeScreen
import com.pemmob.eksplorasianime.ui.viewmodel.AnimeViewModel

/**
 * Pengaturan NavHost Jetpack Compose.
 * Menghubungkan Home Screen dan Detail Screen secara deklaratif.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: AnimeViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Screen 1: Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAnimeClick = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // Screen 2: Anime Detail Screen
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("animeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            DetailScreen(
                animeId = animeId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
