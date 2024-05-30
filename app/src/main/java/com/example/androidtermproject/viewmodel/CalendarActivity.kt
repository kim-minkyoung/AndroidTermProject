package com.example.androidtermproject.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtermproject.R
import com.example.androidtermproject.adapter.DrawerAdapter
import com.example.androidtermproject.databinding.ActivityCalendarBinding
import com.example.androidtermproject.databinding.CalendarDrawerLayoutBinding
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityCalendarBinding // 이 줄 추가
    private lateinit var drawerBinding: CalendarDrawerLayoutBinding
    private lateinit var drawerListBinding: CalendarDrawerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater) // 이 줄 추가
        setContentView(binding.root)

        val fromProfile = intent.getBooleanExtra("FROM_PROFILE", false)

        if (fromProfile) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            setupDrawer()
        }

        val moveToDiaryButton = binding.root.findViewById<Button>(R.id.moveToDiaryButton)
        moveToDiaryButton.text = "Move to Diary"
        moveToDiaryButton.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupDrawer() {
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        drawerBinding.myProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        drawerBinding.profileRecyclerView.layoutManager = LinearLayoutManager(this)
        drawerBinding.profileRecyclerView.adapter = DrawerAdapter(friendsProfile)

        drawerBinding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
        drawerBinding.withdrawButton.setOnClickListener {
            showWithdrawDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("로그아웃 하시겠습니까?")
        builder.setPositiveButton("로그아웃") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun showWithdrawDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("탈퇴")

        val input = EditText(this)
        input.maxLines = 5
        input.hint = "탈퇴 사유 (100글자 제한)"
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("탈퇴") { dialog, _ ->
            val withdrawReason = input.text.toString()
            dialog.dismiss()
        }
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    data class ProfileData(val img: Int, val name: String, val color: Int?)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (intent.getBooleanExtra("FROM_PROFILE", false)) {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
                return true
            }
        } else {
            if (toggle.onOptionsItemSelected(item)) return true // 이 줄 수정
        }
        return super.onOptionsItemSelected(item)
    }
}
