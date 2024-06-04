package com.example.androidtermproject.viewmodel

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.databinding.ActivityLoginBinding
import com.example.androidtermproject.viewmodel.CalendarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
                    val user = auth.currentUser
                    val uid = user?.uid

                    // 사용자 프로필 문서 생성
                    if (uid != null) {
                        val userDocRef = db.collection("users").document(uid)
                        val userData = hashMapOf(
                            "userName" to email,
                        )
                        userDocRef.set(userData)
                            .addOnSuccessListener {
                                // 사용자 프로필 생성 성공
                                // 프로필 문서에 필요한 데이터 추가
                                val profileData = hashMapOf(
                                    "profileName" to email,
                                    "profileComment" to "",
                                    "profileContact" to "",
                                    "profileImageUrl" to ""
                                )
                                userDocRef.collection("profile").document("userProfile").set(profileData)
                                    .addOnSuccessListener {
                                        // 프로필 데이터 추가 성공
                                        Toast.makeText(this, "Account created and signed in.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, CalendarActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        // 프로필 데이터 추가 실패
                                        Log.e(TAG, "Error creating user profile data", e)
                                        binding.errorMessage.text = "Sign up failed: ${e.message}"
                                    }
                            }
                            .addOnFailureListener { e ->
                                // 사용자 프로필 생성 실패
                                Log.e(TAG, "Error creating user profile document", e)
                                binding.errorMessage.text = "Sign up failed: ${e.message}"
                            }
                    }

                } else {
                    // 회원가입 실패
                    binding.errorMessage.text = "Sign up failed: ${task.exception?.message}"
                }
            }
    }

}
