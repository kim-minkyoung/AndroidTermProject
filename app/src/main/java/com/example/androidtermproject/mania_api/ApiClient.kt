package com.example.androidtermproject.mania_api
// ApiClient.kt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.maniadb.com/"

    val instance: ManiaDBService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ManiaDBService::class.java)
    }
}
