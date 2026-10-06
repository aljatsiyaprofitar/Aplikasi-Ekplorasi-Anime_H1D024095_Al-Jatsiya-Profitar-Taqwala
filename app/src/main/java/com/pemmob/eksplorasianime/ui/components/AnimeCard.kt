package com.pemmob.eksplorasianime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.ui.theme.GoldStar

/**
 * Komponen Card untuk item anime pada LazyColumn.
 * Menampilkan minimal 4 data wajib:
 * 1. Judul anime
 * 2. Rating (Score / Age Rating)
 * 3. Tahun rilis
 * 4. Jumlah episode
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar/Badge Visual (tanpa perlu download gambar sesuai aturan 3.4)
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Rounded.Movie,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = anime.type ?: "TV",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Informasi Utama Anime
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 1. Judul Anime (Wajib)
                Text(
                    text = anime.displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Baris Badge: 2. Rating (Score), 3. Tahun Rilis, 4. Jumlah Episode (Wajib)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Rating / Skor
                    InfoBadge(
                        text = anime.displayScore,
                        icon = Icons.Rounded.Star,
                        containerColor = GoldStar.copy(alpha = 0.18f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )

                    // Tahun Rilis
                    InfoBadge(
                        text = anime.displayYear,
                        icon = Icons.Rounded.CalendarToday,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Jumlah Episode
                    InfoBadge(
                        text = anime.displayEpisodes,
                        icon = Icons.Rounded.Movie,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Batas Usia / Rating Usia jika ada
                    anime.rating?.let { ageRating ->
                        val shortRating = ageRating.split("-").firstOrNull()?.trim() ?: ageRating
                        InfoBadge(
                            text = shortRating,
                            icon = Icons.Rounded.VerifiedUser,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Navigasi Arrow Icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Lihat Detail",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Light Mode")
@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
private fun AnimeCardPreview() {
    com.pemmob.eksplorasianime.ui.theme.AplikasiEksplorasiAnimeTheme {
        AnimeCard(
            anime = Anime(
                malId = 1,
                title = "Cowboy Bebop",
                titleEnglish = "Cowboy Bebop",
                score = 8.75,
                rating = "R - 17+ (violence & profanity)",
                year = 1998,
                episodes = 26,
                type = "TV"
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
