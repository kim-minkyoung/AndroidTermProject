package com.example.androidtermproject

import ProfileAdapter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtermproject.databinding.ActivityCalendarBinding
import com.example.androidtermproject.databinding.CalendarDrawerLayoutBinding

class CalendarActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityCalendarBinding
    lateinit var drawerBinding: CalendarDrawerLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        drawerBinding = binding.calendarDrawerLayout
        val myProfileData = ProfileData(
            img = R.drawable.my_default_profile,
            name = "Profile 1",
            color = ContextCompat.getColor(this, R.color.purplePoint)
        )

        val friendsProfile = listOf(
            myProfileData,
            ProfileData(
                img = R.drawable.friend_default_profile,
                name = "Friend 2",
                color = ContextCompat.getColor(this, R.color.transparent)
            )
        )

        drawerBinding.friendsProfile = friendsProfile

        drawerBinding.profileRecyclerView.layoutManager = LinearLayoutManager(this)
        drawerBinding.profileRecyclerView.adapter = ProfileAdapter(friendsProfile)
    }

    data class ProfileData(val img: Int, val name: String, val color: Int?)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
