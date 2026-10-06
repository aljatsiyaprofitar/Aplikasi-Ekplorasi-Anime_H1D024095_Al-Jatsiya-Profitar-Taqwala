package com.pemmob.eksplorasianime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.ui.components.ErrorView
import com.pemmob.eksplorasianime.ui.components.InfoBadge
import com.pemmob.eksplorasianime.ui.components.LoadingView
import com.pemmob.eksplorasianime.ui.state.UiState
import com.pemmob.eksplorasianime.ui.theme.GoldStar
import com.pemmob.eksplorasianime.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    animeId: Int,
    viewModel: AnimeViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detailState by viewModel.animeDetailState.collectAsState()

    // Ambil data detail saat screen pertama kali dibuka atau jika animeId berubah
    LaunchedEffect(animeId) {
        viewModel.fetchAnimeDetail(animeId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Anime",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali ke Beranda"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = detailState) {
                is UiState.Loading -> {
                    LoadingView(message = "Memuat rincian anime...")
                }
                is UiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.fetchAnimeDetail(animeId) }
                    )
                }
                is UiState.Success -> {
                    AnimeDetailContent(anime = state.data)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeDetailContent(
    anime: Anime,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kartu Utama Header Anime
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Judul Utama (Wajib)
                Text(
                    text = anime.displayTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Judul Asli / Jepang jika tersedia
                anime.titleJapanese?.let { jTitle ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = jTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Baris Badge Data Minimal (Rating, Tahun Rilis, Jumlah Episode)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Rating / Skor
                    InfoBadge(
                        text = "${anime.displayScore} / 10",
                        icon = Icons.Rounded.Star,
                        containerColor = GoldStar.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )

                    // Tahun Rilis
                    InfoBadge(
                        text = "Rilis: ${anime.displayYear}",
                        icon = Icons.Rounded.CalendarToday,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // Jumlah Episode
                    InfoBadge(
                        text = anime.displayEpisodes,
                        icon = Icons.Rounded.VideoLibrary,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    // Rating Usia
                    InfoBadge(
                        text = anime.displayAgeRating,
                        icon = Icons.Rounded.VerifiedUser,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Kartu Informasi Teknis Tambahan (Tipe, Status, Durasi, Studio)
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Informasi Produksi",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                InfoRow(label = "Tipe Media", value = anime.type ?: "TV", icon = Icons.Rounded.Tv)
                InfoRow(label = "Status", value = anime.status ?: "Unknown", icon = Icons.Rounded.CheckCircle)
                InfoRow(label = "Durasi", value = anime.duration ?: "N/A", icon = Icons.Rounded.Schedule)
                InfoRow(label = "Studio", value = anime.displayStudios, icon = Icons.Rounded.Movie)
            }
        }

        // Kartu Genre Anime (jika ada)
        anime.genres?.takeIf { it.isNotEmpty() }?.let { genreList ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Genre",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genreList.forEach { genre ->
                            AssistChip(
                                onClick = {},
                                label = { Text(genre.name) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }

        // Kartu Sinopsis
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Sinopsis Cerita",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = anime.displaySynopsis,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Detail Light")
@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Detail Dark"
)
@Composable
private fun AnimeDetailPreview() {
    com.pemmob.eksplorasianime.ui.theme.AplikasiEksplorasiAnimeTheme {
        AnimeDetailContent(
            anime = Anime(
                malId = 1,
                title = "Cowboy Bebop",
                titleJapanese = "カウボーイビバップ",
                score = 8.75,
                rating = "R - 17+ (violence & profanity)",
                year = 1998,
                episodes = 26,
                type = "TV",
                status = "Finished Airing",
                duration = "24 min per ep",
                synopsis = "Crime is timeless. In the year 2071, Spike Spiegel and Jet Black pursue bounties across space aboard the Bebop...",
                genres = listOf(
                    com.pemmob.eksplorasianime.data.model.Genre(name = "Action"),
                    com.pemmob.eksplorasianime.data.model.Genre(name = "Sci-Fi")
                ),
                studios = listOf(
                    com.pemmob.eksplorasianime.data.model.Studio(name = "Sunrise")
                )
            )
        )
    }
}
