package com.example.androidtermproject.mania_api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ManiaDBService {
    @GET("api/search/fire/?sr=song&display=1&key=horse6953@naver.com&v=0.5")
    fun searchSongs(
//        @Path("song") song: String,
//        @Query("sr") sr: String = "song",
//        @Query("display") display: Int = 10,
//        @Query("key") key: String = "horse6953@naver.com",
//        @Query("v") version: String = "0.5"
    ): Call<ResponseBody>
}
