package com.pemmob.eksplorasianime.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object untuk menyediakan instance Retrofit dan ApiService.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.tenrai.org/v1/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
