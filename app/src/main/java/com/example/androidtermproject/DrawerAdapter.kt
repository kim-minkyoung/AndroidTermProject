import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.CalendarActivity.ProfileData
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class DrawerAdapter(private val Drawers: List<ProfileData>) : RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDrawerListBinding.inflate(inflater, parent, false)
        return DrawerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        val Drawer = Drawers[position]
        holder.bind(Drawer)
    }

    override fun getItemCount(): Int = Drawers.size

    inner class DrawerViewHolder(private val binding: CalendarDrawerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileData) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }
}
