package com.example.androidtermproject.viewmodel

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityDiaryBinding
import com.example.androidtermproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private var currentUserId: String = ""
    private var diaryBeforeEdit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""

        supportActionBar?.apply {
            title = "Diary"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        setupButtonListeners()

        diaryBeforeEdit = binding.diaryText.text.toString()
    }

    override fun onResume() {
        super.onResume()
        loadDiaryData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }

    private fun loadDiaryData() {
        // Load profile data from Firestore and display it in UI
        db.collection("users").document(currentUserId)
            .get()
            .addOnSuccessListener { document ->
                val diaryText = document.getString("diaryText") ?: ""

                binding.diaryText.text = diaryText

                switchEditMode(false)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load diary data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupButtonListeners() {

        binding.diaryLeftButton.setOnClickListener {
            if (binding.diaryText.visibility == View.GONE) {
                switchEditMode(false)
            } else {
//                val intent = Intent(this, CalendarActivity::class.java)
//                intent.putExtra("FROM_PROFILE", true)
//                startActivity(intent)
            }
        }

        binding.diaryRightButton.setOnClickListener {
            when (binding.diaryRightButton.text) {
                "Save" -> {
                    saveDiary()
                }
                "Edit" -> {
                    switchEditMode(true)
                }
            }
        }
    }

    private fun saveDiary() {
        val user = auth.currentUser
        if (user != null) {
            val userDiary = mapOf(
                "diaryText" to binding.diaryTextEdit.text.toString()
            )

            db.collection("users").document(user.uid)
                .update(userDiary)
                .addOnSuccessListener {
                    Toast.makeText(this, "Diary Updated", Toast.LENGTH_SHORT).show()
                    binding.diaryText.text = binding.diaryTextEdit.text
                    diaryBeforeEdit = binding.diaryText.text.toString()
                    switchEditMode(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun switchEditMode(isEditMode: Boolean) {
        if (isEditMode) {
            binding.diaryText.visibility = View.GONE
            binding.diaryTextEdit.visibility = View.VISIBLE
            binding.diaryTextEdit.setText(binding.diaryText.text)

            binding.diaryLeftButton.text = "Cancel"
            binding.diaryRightButton.text = "Save"
        } else {
            binding.diaryText.visibility = View.VISIBLE
            binding.diaryTextEdit.visibility = View.GONE
            binding.diaryTextEdit.setText(binding.diaryText.text)

            binding.diaryLeftButton.text = "Cancel"
            binding.diaryRightButton.text = "Edit"
        }
    }
}
