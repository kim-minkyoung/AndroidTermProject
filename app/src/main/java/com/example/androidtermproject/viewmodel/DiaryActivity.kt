package com.example.androidtermproject.viewmodel

// DiaryActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.R

class DiaryActivity : AppCompatActivity() {

    private lateinit var findMusicButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        findMusicButton = findViewById(R.id.moveToDiaryButton)

        findMusicButton.setOnClickListener {
            val intent = Intent(this, MusicSearchActivity::class.java)
            startActivity(intent)
        }
    }
}
