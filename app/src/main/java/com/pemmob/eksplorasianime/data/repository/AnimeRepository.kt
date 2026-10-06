package com.pemmob.eksplorasianime.data.repository

import com.pemmob.eksplorasianime.data.model.Anime

/**
 * Interface abstraksi untuk data layer (Repository pattern).
 * Memisahkan logika pengambilan data dari ViewModel.
 */
interface AnimeRepository {
    suspend fun getAnimeList(query: String? = null): Result<List<Anime>>
    suspend fun getAnimeDetail(id: Int): Result<Anime>
}
