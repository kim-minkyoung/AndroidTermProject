package com.example.androidtermproject.viewmodel

import MusicBuddyAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.MusicBuddy
import com.example.androidtermproject.mania_api.MusicItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FindOtherActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var musicBuddyAdapter: MusicBuddyAdapter
    private val musicBuddyList = mutableListOf<MusicBuddy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_others)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 액션바 설정
        supportActionBar?.apply {
            title = "Find Others"
            setDisplayHomeAsUpEnabled(true)
        }

        // 인텐트에서 MusicItem 객체 가져오기
        val selectedMusic = intent.getSerializableExtra("selectedMusic") as? MusicItem

        // UI 요소 초기화
        val musicImageView = findViewById<ImageView>(R.id.musicImage)
        val musicTitleTextView = findViewById<TextView>(R.id.musicTitle)
        val musicArtistTextView = findViewById<TextView>(R.id.musicArtist)

        selectedMusic?.let {
            musicTitleTextView.text = it.title
            musicArtistTextView.text = it.artist
            Glide.with(this).load(it.albumImage).error(R.drawable.default_music_img).into(musicImageView)
        }

        // 선택된 음악이 유효한 경우에만 사용자 음악 버디를 로드
        selectedMusic?.let { loadUsersMusicBuddies(it) }
    }

    private fun loadUsersMusicBuddies(selectedMusic: MusicItem) {
        val usersCollectionRef = db.collection("users")

        usersCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(this@FindOtherActivity, "같은 음악을 담고 있는 사용자가 없습니다", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                for (userDocument in querySnapshot.documents) {
                    val diariesCollectionRef = userDocument.reference.collection("diaries")

                    diariesCollectionRef.get()
                        .addOnSuccessListener { diariesQuerySnapshot ->
                            for (diaryDocument in diariesQuerySnapshot.documents) {
                                val musicTitle = diaryDocument.getString("musicTitle")
                                val musicArtist = diaryDocument.getString("musicArtist")

                                if (musicTitle == selectedMusic.title && musicArtist == selectedMusic.artist) {
                                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                                    val profileRef = userDocument.reference.collection("profile")

                                    profileRef.get()
                                        .addOnSuccessListener { profileSnapshot ->
                                            // 프로필 서브컬렉션에 문서가 있는지 확인
                                            if (profileSnapshot.isEmpty) {
                                                Toast.makeText(this@FindOtherActivity, "불러오지 못한 프로필이 있습니다", Toast.LENGTH_SHORT).show()
                                                return@addOnSuccessListener
                                            }

                                            // 프로필 서브컬렉션에 있는 문서를 반복하여 처리
                                            for (profileDocument in profileSnapshot.documents) {
                                                if (userDocument.id != currentUserUid) {
                                                    // 프로필 문서에서 원하는 필드 가져오기
                                                    val userName =
                                                        profileDocument.getString("profileName")
                                                            ?: ""
                                                    val userComment =
                                                        profileDocument.getString("profileComment")
                                                            ?: ""
                                                    val userContact =
                                                        profileDocument.getString("profileContact")
                                                            ?: ""
                                                    val subTitle =
                                                        "comment: ${userComment}\ncontact: ${userContact}"
                                                    val profileImage =
                                                        profileDocument.getString("profileImageUrl")
                                                            ?: "https://via.placeholder.com/150"

                                                    // 가져온 값으로 MusicBuddy 객체 생성하여 리스트에 추가
                                                    musicBuddyList.add(
                                                        MusicBuddy(
                                                            userName,
                                                            subTitle,
                                                            profileImage
                                                        )
                                                    )
                                                }
                                            }

                                            // RecyclerView 업데이트
                                            updateRecyclerView()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("FindOtherActivity", "Error getting profile documents for user: ${userDocument.id}", e)
                                        }
                                    break
                                }
                            }

                            // 모든 다이어리 문서 처리 후 RecyclerView 업데이트
//                            updateRecyclerView()

                        }
                        .addOnFailureListener { e ->
                            Log.e("FindOtherActivity", "Error getting diaries documents for user: ${userDocument.id}", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FindOtherActivity", "Error getting users documents", e)
            }
    }



    private fun updateRecyclerView() {
        recyclerView = findViewById(R.id.buddyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        musicBuddyAdapter = MusicBuddyAdapter(this, musicBuddyList)
        recyclerView.adapter = musicBuddyAdapter
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
