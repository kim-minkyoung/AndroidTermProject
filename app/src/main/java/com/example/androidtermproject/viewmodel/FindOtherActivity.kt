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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        supportActionBar?.apply {
            title = "Find Others"
            setDisplayHomeAsUpEnabled(true)
        }

        val selectedMusic = intent.getSerializableExtra("selectedMusic") as? MusicItem

        val musicImageView = findViewById<ImageView>(R.id.musicImage)
        val musicTitleTextView = findViewById<TextView>(R.id.musicTitle)
        val musicArtistTextView = findViewById<TextView>(R.id.musicArtist)

        selectedMusic?.let {
            musicTitleTextView.text = it.title
            musicArtistTextView.text = it.artist
            Glide.with(this).load(selectedMusic.albumImage).error(R.drawable.default_music_img).into(musicImageView)
        }

        loadUsersMusicBuddies(selectedMusic!!)
    }

    private fun loadUsersMusicBuddies(selectedMusic: MusicItem) {
        val usersCollectionRef = db.collection("users")

        usersCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (userDocument in querySnapshot.documents) {
                    val diariesCollectionRef = userDocument.reference.collection("diaries")
                    println("diariesCollectionRef: ${diariesCollectionRef}")
                    diariesCollectionRef.get()
                        .addOnSuccessListener { diariesQuerySnapshot ->
                            for (diaryDocument in diariesQuerySnapshot.documents) {
                                val musicTitle = diaryDocument.getString("musicTitle")
                                val musicArtist = diaryDocument.getString("musicArtist")

                                if (musicTitle == selectedMusic.title && musicArtist == selectedMusic.artist) {
                                    val userName = userDocument.id
                                    musicBuddyList.add(MusicBuddy(userName, "", "https://via.placeholder.com/150"))
                                    break
                                }
                            }

                            recyclerView = findViewById(R.id.buddyRecyclerView)
                            recyclerView.layoutManager = LinearLayoutManager(this)
                            musicBuddyAdapter = MusicBuddyAdapter(this, musicBuddyList)
                            recyclerView.adapter = musicBuddyAdapter
                            
                            Log.d("TAG", "Successfully retrieved user documents")
                            Toast.makeText(this@FindOtherActivity, "successed", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@FindOtherActivity, "failed", Toast.LENGTH_SHORT).show()
                            Log.e("TAG", "Error getting diaries documents", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting users documents", e)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
