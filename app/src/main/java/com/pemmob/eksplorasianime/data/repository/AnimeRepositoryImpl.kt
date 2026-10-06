package com.pemmob.eksplorasianime.data.repository

import com.pemmob.eksplorasianime.data.model.Anime
import com.pemmob.eksplorasianime.data.remote.ApiService
import com.pemmob.eksplorasianime.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepositoryImpl(
    private val apiService: ApiService = RetrofitClient.apiService
) : AnimeRepository {

    override suspend fun getAnimeList(query: String?): Result<List<Anime>> {
        return withContext(Dispatchers.IO) {
            try {
                val searchQuery = query?.trim()?.takeIf { it.isNotEmpty() }
                val response = apiService.getAnimeList(query = searchQuery)
                Result.success(response.data)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAnimeDetail(id: Int): Result<Anime> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAnimeDetail(id = id)
                Result.success(response.data)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
