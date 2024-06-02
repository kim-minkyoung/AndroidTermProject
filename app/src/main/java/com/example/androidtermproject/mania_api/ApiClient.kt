package com.example.androidtermproject.mania_api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://www.maniadb.com/"


    val instance: ManiaDBService by lazy {
        val logging = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()
        val gson = GsonBuilder().setLenient().create()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson)) // GsonConverterFactory 추가
            .build()
        retrofit.create(ManiaDBService::class.java)
    }
}
