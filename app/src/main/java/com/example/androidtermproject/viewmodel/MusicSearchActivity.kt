package com.example.androidtermproject.viewmodel

import MusicAdapter
import MusicResponse
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.ApiClient
import com.example.androidtermproject.mania_api.MusicItem
import com.example.androidtermproject.mania_api.Song
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.simpleframework.xml.core.Persister
import org.xml.sax.InputSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class MusicSearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_search)

        supportActionBar?.apply {
            title = "Music Search"
            setDisplayHomeAsUpEnabled(true)
        }

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val musicItems = listOf(
            MusicItem("", "", "album_image_url_1"),
            MusicItem("", "", "album_image_url_2"),
            MusicItem("", "", "album_image_url_2"),
            MusicItem("", "", "album_image_url_2"),
            MusicItem("", "", "album_image_url_2"),
            MusicItem("", "", "album_image_url_2"),
            // 추가적인 음악 항목들을 여기에 추가할 수 있습니다.
        )

        musicAdapter = MusicAdapter(musicItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = musicAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchSongs(query)
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchSongs(query:String){
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
        musicAdapter = MusicAdapter(musicItemList)
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

    private fun displayMusicInfo(
        title: String,
        description: String,
        albumTitle: String,
        albumImage: String,
        artistName: String
    ) {
        // 화면에 표시할 뷰들을 가져옵니다.
        val songTitleTextView = findViewById<TextView>(R.id.songTitleTextView)
        val songArtistTextView = findViewById<TextView>(R.id.songArtistTextView)
        val albumImageView = findViewById<ImageView>(R.id.songImageView)

        // 노래 제목, 아티스트 텍스트뷰에 데이터 설정
        songTitleTextView.text = title
        songArtistTextView.text = artistName

        // 앨범 이미지 설정 (Glide 등의 라이브러리를 사용하여 이미지 로드)
        Glide.with(this).load(albumImage).into(albumImageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}