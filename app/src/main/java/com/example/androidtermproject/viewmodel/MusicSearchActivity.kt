package com.example.androidtermproject.viewmodel

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.R
import com.example.androidtermproject.adapter.MusicAdapter
import com.example.androidtermproject.mania_api.ApiClient
import com.example.androidtermproject.mania_api.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        musicAdapter = MusicAdapter()
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

    private fun searchSongs(query: String) {
        ApiClient.instance.searchSongs().enqueue(object : Callback<List<Song>> {
            override fun onResponse(call: Call<List<Song>>, response: Response<List<Song>>) {
                if (response.isSuccessful) {
                    val songs = response.body()
                    if (songs != null) {
                        musicAdapter.updateSongs(songs)
                    } else {
                        Toast.makeText(this@MusicSearchActivity, "No songs found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MusicSearchActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Song>>, t: Throwable) {
                Log.e("MusicSearchActivity", "Failed to fetch data: ${t.message}", t)
                Toast.makeText(this@MusicSearchActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
