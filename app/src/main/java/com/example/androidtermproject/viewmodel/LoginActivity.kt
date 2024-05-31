package com.example.androidtermproject.viewmodel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.databinding.ActivityLoginBinding
import com.example.androidtermproject.viewmodel.CalendarActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val id = binding.email.text.toString()
            val password = binding.password.text.toString()

            signIn(id, password)
        }

        binding.signupButton.setOnClickListener {
            val id = binding.email.text.toString()
            val password = binding.password.text.toString()

            signUp(id, password)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                } else {
                    // 로그인 실패
                    binding.errorMessage.text = "Authentication failed: ${task.exception?.message}"
                }
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    Toast.makeText(this, "Account created and signed in.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                } else {
                    // 회원가입 실패
                    binding.errorMessage.text = "Sign up failed: ${task.exception?.message}"
                }
            }
    }
}
