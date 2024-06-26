package com.example.androidtermproject.viewmodel

import MusicAdapter
import MusicResponse
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityMusicSearchBinding
import com.example.androidtermproject.mania_api.ApiClient
import com.example.androidtermproject.mania_api.MusicItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import okhttp3.ResponseBody
import org.simpleframework.xml.core.Persister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicSearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var binding: ActivityMusicSearchBinding
    private var selectedMusic: MusicItem? = null
    private lateinit var selectedDate: String

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""

        // 인텐트로부터 선택된 날짜를 가져옴
        selectedDate = intent.getStringExtra("selectedDate") ?: ""

        supportActionBar?.apply {
            title = "Music Search"
            setDisplayHomeAsUpEnabled(true)
        }

        searchEditText = binding.searchEditText
        searchButton = binding.searchButton
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val musicItems = listOf(
            MusicItem("", "", "album_image_url_1"),
            MusicItem("", "", "album_image_url_2"),
            MusicItem("", "", "album_image_url_3"),
            MusicItem("", "", "album_image_url_4"),
            MusicItem("", "", "album_image_url_5"),
        )

        val moveToDiaryButton = findViewById<Button>(R.id.moveToDiaryButton)
        moveToDiaryButton.text = "Save Music"
        moveToDiaryButton.setBackgroundResource(R.drawable.button_purple_radious)
        moveToDiaryButton.setOnClickListener {
            if (selectedMusic != null) {
                saveSelectedMusicToFirebase()
                finish()
            } else {
                Toast.makeText(this, "음악을 선택하세요", Toast.LENGTH_SHORT).show()
            }
        }

        musicAdapter = MusicAdapter(musicItems) { musicItem ->
            selectedMusic = musicItem
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = musicAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchSongs(query)
            } else {
                Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSelectedMusicToFirebase() {
        val user = auth.currentUser
        if (user != null && selectedMusic != null) {
            val diaryEntry = mapOf(
                "musicTitle" to selectedMusic?.title,
                "musicArtist" to selectedMusic?.artist,
                "musicAlbumImage" to selectedMusic?.albumImage
            )

            db.collection("users").document(user.uid)
                .collection("diaries").document(selectedDate)
                .set(diaryEntry, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "음악이 저장되었습니다", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w("MusicSearchActivity", "Error adding document", e)
                    Toast.makeText(this, "음악 저장에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun searchSongs(query: String) {
        ApiClient.instance.searchSongs(query).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    if (responseBody != null) {
                        val musicResponse = parseXmlMusicResponse(responseBody)
                        println("응답인 " + musicResponse.toString())
                        if (musicResponse != null) {
                            val items = musicResponse.channel?.items
                            if (items != null && items.isNotEmpty()) {
                                val musicItemList = items.map { item ->
                                    MusicItem(
                                        title = item.title ?: "",
                                        artist = item.artist?.name ?: "",
                                        albumImage = item.album?.image ?: ""
                                    )
                                }
                                displayMusicInfo(musicItemList)
                            } else {
                                Toast.makeText(this@MusicSearchActivity, "No songs found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@MusicSearchActivity, "Response body is empty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MusicSearchActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("MusicSearchActivity", "Failed to fetch data: ${t.message}", t)
                Toast.makeText(this@MusicSearchActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayMusicInfo(musicItemList: List<MusicItem>) {
        musicAdapter = MusicAdapter(musicItemList) { musicItem ->
            selectedMusic = musicItem
        }
        recyclerView.adapter = musicAdapter
    }

    private fun parseXmlMusicResponse(xmlString: String): MusicResponse? {
        println("출력 잘되니? " + xmlString)
        val serializer = Persister()
        return try {
            serializer.read(MusicResponse::class.java, xmlString)
        } catch (e: Exception) {
            Log.e("MusicSearchActivity", "Failed to parse XML: ${e.message}", e)
            null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
