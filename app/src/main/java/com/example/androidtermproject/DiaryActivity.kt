package com.example.androidtermproject


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DiaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val footerButton = findViewById<Button>(R.id.moveToDiaryButton)

        // 버튼 배경 색상 변경
        footerButton.setBackgroundResource(R.drawable.button_purple_radious)
        footerButton.text = "Select Music & Create Diary"

        // 버튼 클릭 이벤트 설정
        footerButton.setOnClickListener {
            // 노래 선택을 위한 액티비티 실행
        }
    }

}
