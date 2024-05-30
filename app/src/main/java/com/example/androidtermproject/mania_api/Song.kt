package com.example.androidtermproject.mania_api

// Song.kt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val title: String,
    val artist: String
) : Parcelable
