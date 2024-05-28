package com.example.androidtermproject

// DiaryActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

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
