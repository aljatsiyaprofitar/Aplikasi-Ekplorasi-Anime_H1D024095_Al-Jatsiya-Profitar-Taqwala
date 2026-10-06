package com.pemmob.eksplorasianime.data.remote

import com.pemmob.eksplorasianime.data.model.AnimeDetailResponse
import com.pemmob.eksplorasianime.data.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("anime")
    suspend fun getAnimeList(
        @Query("q") query: String? = null,
        @Query("limit") limit: Int = 25
    ): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): AnimeDetailResponse
}
