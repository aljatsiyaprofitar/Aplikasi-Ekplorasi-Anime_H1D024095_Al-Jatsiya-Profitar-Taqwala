package com.pemmob.eksplorasianime.data.remote

import com.pemmob.eksplorasianime.data.model.AnimeDetailResponse
import com.pemmob.eksplorasianime.data.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface Retrofit untuk komunikasi dengan Tenrai REST API.
 * Base URL: https://api.tenrai.org/v1/
 */
interface ApiService {

    /**
     * Mengambil daftar anime secara dinamis.
     * Mendukung pencarian opsional dengan parameter query `q`.
     */
    @GET("anime")
    suspend fun getAnimeList(
        @Query("q") query: String? = null,
        @Query("limit") limit: Int = 25
    ): AnimeResponse

    /**
     * Mengambil detail anime berdasarkan ID (mal_id).
     */
    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): AnimeDetailResponse
}
