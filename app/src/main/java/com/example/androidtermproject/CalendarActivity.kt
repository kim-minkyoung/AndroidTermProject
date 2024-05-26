package com.example.androidtermproject;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityCalendarBinding
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class CalendarActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val myProfileData = ProfileData(
            img = R.drawable.my_default_profile,
            name = "My Profile",
            color = ContextCompat.getColor(this, R.color.purplePoint)
        )

        // 데이터 바인딩
        binding.apply {
            var profileData = myProfileData
//            executePendingBindings()
        }
    }

    data class ProfileData(val img: Int, val name: String, val color: Int?)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO : Drawer 제어
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
