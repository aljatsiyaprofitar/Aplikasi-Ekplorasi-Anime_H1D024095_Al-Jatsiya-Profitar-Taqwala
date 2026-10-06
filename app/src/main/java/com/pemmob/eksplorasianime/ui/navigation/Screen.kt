package com.pemmob.eksplorasianime.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int): String = "detail/$animeId"
    }
}
