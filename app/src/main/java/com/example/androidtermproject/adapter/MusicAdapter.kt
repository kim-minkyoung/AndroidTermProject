import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.mania_api.MusicItem

class MusicAdapter(
    private val items: List<MusicItem>,
    private val onItemClick: (MusicItem) -> Unit // 클릭 콜백 추가
) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_block, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MusicItem, position: Int) {
            val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
            val artistTextView = itemView.findViewById<TextView>(R.id.subTitleTextView)
            val albumImageView = itemView.findViewById<ImageView>(R.id.songImageView)

            titleTextView.text = item.title
            artistTextView.text = item.artist
            Glide.with(itemView.context).load(item.albumImage).into(albumImageView)

            itemView.setOnClickListener {
                if (item.title.isNotEmpty()) {
                    if (selectedItemPosition != RecyclerView.NO_POSITION) {
                        val previousSelectedView = recyclerView.findViewHolderForAdapterPosition(selectedItemPosition)?.itemView
                        previousSelectedView?.setBackgroundColor(Color.TRANSPARENT)
                    }

                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.purplePoint)) // 보라색으로 배경색 변경
                    selectedItemPosition = adapterPosition
                    onItemClick(item) // 아이템 클릭 콜백 호출
                }
            }

            if (selectedItemPosition == position) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.purplePoint)) // 보라색으로 배경색 변경
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}
