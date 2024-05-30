package com.example.androidtermproject.adapter
// MusicAdapter.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.Song

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    private var songs: List<Song> = listOf()
    var onSongSelected: ((Song) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.songTitleTextView)
        private val artistTextView: TextView = itemView.findViewById(R.id.songArtistTextView)

        fun bind(song: Song) {
            titleTextView.text = song.title
            artistTextView.text = song.artist
            itemView.setOnClickListener {
                onSongSelected?.invoke(song)
            }
        }
    }
}
