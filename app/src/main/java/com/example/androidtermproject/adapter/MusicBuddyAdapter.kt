import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.MusicBuddy
import com.example.androidtermproject.viewmodel.ProfileActivity

class MusicBuddyAdapter(private val context: Context, private val musicBuddyList: List<MusicBuddy>) :
    RecyclerView.Adapter<MusicBuddyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val songArtistTextView: TextView = itemView.findViewById(R.id.subTitleTextView)
        private val profileImageView: ImageView = itemView.findViewById(R.id.songImageView)

        fun bind(musicBuddy: MusicBuddy) {
            songTitleTextView.text = musicBuddy.name
            songArtistTextView.text = musicBuddy.description

            Glide.with(context)
                .load(R.drawable.user)
                .into(profileImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_block, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val musicBuddy = musicBuddyList[position]
        holder.bind(musicBuddy)
    }

    override fun getItemCount(): Int {
        return musicBuddyList.size
    }
}
