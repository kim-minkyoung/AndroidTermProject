package com.example.androidtermproject.mania_api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ManiaDBService {
    @GET("api/search/{song}}/?sr=song&display=5&key=horse6953@naver.com&v=0.5")
    fun searchSongs(
        @Path("song") song: String,
    ): Call<ResponseBody>
}
