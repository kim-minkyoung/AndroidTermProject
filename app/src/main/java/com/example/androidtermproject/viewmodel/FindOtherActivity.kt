package com.example.androidtermproject.viewmodel

import MusicBuddyAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.MusicBuddy
import com.example.androidtermproject.mania_api.MusicItem
//import com.squareup.picasso.Picasso
import java.io.Serializable

class FindOtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_others)

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

        val musicBuddyList = mutableListOf<MusicBuddy>()
        musicBuddyList.add(MusicBuddy("friend 1", "description 1", "https://via.placeholder.com/150"))
        musicBuddyList.add(MusicBuddy("friend 2", "description 2", "https://via.placeholder.com/150"))
        musicBuddyList.add(MusicBuddy("friend 3", "description 3", "https://via.placeholder.com/150"))
        musicBuddyList.add(MusicBuddy("friend 4", "description 4", "https://via.placeholder.com/150"))

        val recyclerView = findViewById<RecyclerView>(R.id.buddyRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MusicBuddyAdapter(this, musicBuddyList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
