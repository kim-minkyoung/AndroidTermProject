package com.example.androidtermproject.viewmodel

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityDiaryBinding
import com.example.androidtermproject.mania_api.MusicItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String = ""
    private lateinit var selectedDate: String
    private var diaryBeforeEdit = ""
    private lateinit var selectedMusic: MusicItem // 음악 정보를 저장할 변수 추가


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""

        // 인텐트로부터 선택된 날짜를 가져옴
        selectedDate = intent.getStringExtra("selectedDate") ?: ""
        selectedMusic = intent.getSerializableExtra("selectedMusic") as? MusicItem ?: MusicItem("No Music","Artist - ","")

        val musicTitle = binding.musicTitle
        val musicArtist = binding.musicArtist
        val musicImage = binding.musicImage
        musicTitle.text = selectedMusic.title
        musicArtist.text = selectedMusic.artist
        Glide.with(this).load(selectedMusic.albumImage).error(R.drawable.default_music_img).into(musicImage)

        // TODO
        // selectedDate가 빈 문자열인 경우 오늘 날짜로 설정
        if (selectedDate.isEmpty()) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = sdf.format(Date())
        }
        supportActionBar?.apply {
            title = "Diary"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        setupButtonListeners()
        loadSelectedMusicFromFirebase()
//        loadDiaryData(selectedDate)

        diaryBeforeEdit = binding.diaryText.text.toString()
    }

    //새로고침 시 데이터 새로 로드
    override fun onResume() {
        super.onResume()
        loadSelectedMusicFromFirebase()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }

    private fun loadSelectedMusicFromFirebase() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid)
                .collection("diaries").document(selectedDate)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val musicTitle = documentSnapshot.getString("musicTitle")
                        val musicArtist = documentSnapshot.getString("musicArtist")
                        val musicAlbumImage = documentSnapshot.getString("musicAlbumImage")
                        val diaryText = documentSnapshot.getString("diaryText") ?: ""
                        binding.diaryText.text = diaryText
                        switchEditMode(false)

                        binding.musicTitle.text = musicTitle ?: "No Music"
                        binding.musicArtist.text = musicArtist ?: "Artist -"
                        Glide.with(this@DiaryActivity)
                            .load(musicAlbumImage)
                            .error(R.drawable.default_music_img)
                            .into(binding.musicImage)
                    } else {
                        // 데이터가 없는 경우
                        binding.musicTitle.text = "No Music"
                        binding.musicArtist.text = "Artist -"
                        // 기본 이미지를 설정할 수도 있습니다.
                        Glide.with(this@DiaryActivity)
                            .load(R.drawable.default_music_img)
                            .into(binding.musicImage)
                        Log.d("MusicSearchActivity", "No music data found for selected date")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MusicSearchActivity", "Error fetching music data: ${e.message}", e)
                    Toast.makeText(this@DiaryActivity, "Failed to fetch music data", Toast.LENGTH_SHORT).show()
                }
        }
    }

//    private fun loadDiaryData(date: String) {
//        val user = auth.currentUser
//        if (user != null) {
//            db.collection("users").document(user.uid)
//                .collection("diaries").document(date)
//                .get()
//                .addOnSuccessListener { document ->
//                    val diaryText = document.getString("diaryText") ?: ""
//                    binding.diaryText.text = diaryText
//                    switchEditMode(false)
//                }
//                .addOnFailureListener { exception ->
//                    Toast.makeText(this, "Failed to load diary data: ${exception.message}", Toast.LENGTH_SHORT).show()
//                    Log.d("DiaryActivity", "Failed to load diary data: ${exception.message}")
//                }
//        }
//        else {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun setupButtonListeners() {
        // 음악이 선택된 경우에만 버튼을 "Find Others"로 변경하고 친구를 찾을 수 있는 페이지로 이동하도록 설정합니다.

        if (selectedMusic.title != "No Music") {
            binding.musicSearchFooterButton.text = "Find Others"
            binding.musicSearchFooterButton.setOnClickListener {
                val intent = Intent(this, FindOtherActivity::class.java)
                intent.putExtra("selectedMusic", selectedMusic as Serializable)
                startActivity(intent)
            }
        } else {
            // 음악이 선택되지 않은 경우에는 기존 동작을 유지합니다.
            binding.musicSearchFooterButton.setOnClickListener {
                val intent = Intent(this, MusicSearchActivity::class.java)
                intent.putExtra("selectedDate", selectedDate as Serializable)
                startActivity(intent)
            }
        }

        binding.diaryLeftButton.setOnClickListener {
            if (binding.diaryText.visibility == View.GONE) {
                switchEditMode(false)
            } else {
                showDeleteConfirmationDialog(selectedDate)
            }
        }

        binding.diaryRightButton.setOnClickListener {
            when (binding.diaryRightButton.text) {
                "Save" -> {
                    saveDiary(selectedDate)
                }
                "Edit" -> {
                    switchEditMode(true)
                }
            }
        }
    }

    private fun saveDiary(date: String) {
        val user = auth.currentUser
        if (user != null) {
            val diaryEntry = mapOf(
                "diaryText" to binding.diaryTextEdit.text.toString()

            )

            db.collection("users").document(user.uid)
                .collection("diaries").document(date)
                .set(diaryEntry, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Diary Updated", Toast.LENGTH_SHORT).show()
                    binding.diaryText.text = binding.diaryTextEdit.text
                    switchEditMode(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDeleteConfirmationDialog(date: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("${date}의 다이어리를 삭제하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, id ->
                deleteDiaryData(date)
            })
            .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        builder.create().show()
    }

    private fun deleteDiaryData(date: String) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid)
                .collection("diaries").document(date)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "${date} Diary deleted", Toast.LENGTH_SHORT).show()
                    binding.diaryText.text = ""
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

            binding.diaryLeftButton.text = "Delete"
            binding.diaryRightButton.text = "Edit"
        }
    }
}
