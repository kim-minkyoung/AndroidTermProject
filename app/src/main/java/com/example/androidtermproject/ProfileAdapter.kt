// ProfileAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtermproject.CalendarActivity.ProfileData
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class ProfileAdapter(private val profiles: List<ProfileData>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CalendarDrawerListBinding.inflate(inflater, parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
    }

    override fun getItemCount(): Int = profiles.size

    inner class ProfileViewHolder(private val binding: CalendarDrawerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileData) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }
}
