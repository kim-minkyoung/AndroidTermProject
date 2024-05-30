package com.example.androidtermproject.viewmodel

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

        supportActionBar?.apply {
            title = "Diary"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        findMusicButton = findViewById(R.id.moveToDiaryButton)

        findMusicButton.setOnClickListener {
            val intent = Intent(this, MusicSearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }
}
