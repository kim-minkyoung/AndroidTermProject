package com.example.androidtermproject.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.R

class MusicSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_search)

        supportActionBar?.apply {
            title = "Music Search"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }
}
