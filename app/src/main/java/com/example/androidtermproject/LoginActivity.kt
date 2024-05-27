package com.example.androidtermproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val id = binding.ID.text.toString()
            val password = binding.password.text.toString()

            // 간단한 ID 및 비밀번호 확인 (예제)
            if (id == "123" && password == "123") {
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            } else {
                binding.errorMessage.text = "Invalid ID or Password"
            }
        }
    }
}
