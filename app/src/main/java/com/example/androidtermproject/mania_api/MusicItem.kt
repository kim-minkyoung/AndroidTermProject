package com.example.androidtermproject.mania_api

import java.io.Serializable

data class MusicItem(
    val title: String,
    val artist: String,
    val albumImage: String
) : Serializable
