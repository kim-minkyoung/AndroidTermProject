import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.MusicItem
class MusicAdapter(private val items: List<MusicItem>) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MusicItem) {
            itemView.findViewById<TextView>(R.id.songTitleTextView).text = item.title
            itemView.findViewById<TextView>(R.id.songArtistTextView).text = item.artist
            Glide.with(itemView.context).load(item.albumImage).into(itemView.findViewById<ImageView>(R.id.songImageView))
        }
    }
}
