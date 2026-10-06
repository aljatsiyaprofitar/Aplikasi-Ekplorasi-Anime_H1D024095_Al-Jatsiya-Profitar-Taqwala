package com.pemmob.eksplorasianime.ui.navigation

/**
 * Representasi rute navigasi aplikasi.
 * Sesuai persyaratan: maksimal 2 screens (Home dan Anime Detail).
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int): String = "detail/$animeId"
    }
}
