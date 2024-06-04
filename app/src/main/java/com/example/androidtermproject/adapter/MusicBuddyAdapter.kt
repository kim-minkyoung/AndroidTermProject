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
import com.example.androidtermproject.databinding.ActivityMusicSearchBinding
import com.example.androidtermproject.mania_api.MusicBuddy
import com.example.androidtermproject.viewmodel.ProfileActivity

class MusicBuddyAdapter(private val context: Context, private val musicBuddyList: List<MusicBuddy>) :
    RecyclerView.Adapter<MusicBuddyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitleTextView: TextView = itemView.findViewById(R.id.songTitleTextView)
        private val songArtistTextView: TextView = itemView.findViewById(R.id.songArtistTextView)
        private val profileImageView: ImageView = itemView.findViewById(R.id.songImageView)

        fun bind(musicBuddy: MusicBuddy) {
            songTitleTextView.text = musicBuddy.name
            songArtistTextView.text = musicBuddy.description

            Glide.with(context)
                .load(musicBuddy.profile)
                .error(R.drawable.my_default_profile) // 로드 실패 시 기본 이미지 표시
                .into(profileImageView)

            // 클릭 이벤트 처리
            itemView.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                // 클릭된 친구의 정보를 ProfileActivity로 전달
                intent.putExtra("profileName", musicBuddy.name)
                intent.putExtra("profileComment", musicBuddy.description)
                intent.putExtra("profileImageUrl", musicBuddy.profile)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
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
