package com.example.androidtermproject.mania_api

import java.io.Serializable

data class MusicBuddy(
    val name: String,
    val description: String,
    val profile: String
) : Serializable
