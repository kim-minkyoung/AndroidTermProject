package com.example.androidtermproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        // 사용자의 프로필 정보를 받아올 수 있는 방법이 있다면 해당 정보를 가져와서 아래와 같이 설정할 수 있습니다.
        val profileName = "John Doe"
        // 이미지도 가져와서 설정할 수 있습니다.
        // val profileImage = ...

        binding.profileName.text = profileName
        // binding.profileImage.setImageDrawable(profileImage) // 이미지 설정 예시
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }
}
