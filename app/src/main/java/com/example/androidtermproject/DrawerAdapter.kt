import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.CalendarActivity.ProfileData
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class DrawerAdapter(private val profiles: List<ProfileData>) : RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDrawerListBinding.inflate(inflater, parent, false)
        return DrawerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        val profile = profiles[position]
        holder.binding.profile = profile
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = profiles.size

    inner class DrawerViewHolder(val binding: CalendarDrawerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileData) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }
}
