package com.pemmob.eksplorasianime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pemmob.eksplorasianime.ui.navigation.AppNavigation
import com.pemmob.eksplorasianime.ui.theme.AplikasiEksplorasiAnimeTheme

/**
 * Main Activity aplikasi sebagai single-activity entry point.
 * Sepenuhnya menggunakan Jetpack Compose tanpa XML Layout.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiEksplorasiAnimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
