package com.example.androidtermproject.mania_api
// ManiaDBService.kt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ManiaDBService {
    @GET("search")
    fun searchSongs(@Query("q") query: String): Call<List<Song>>
}
