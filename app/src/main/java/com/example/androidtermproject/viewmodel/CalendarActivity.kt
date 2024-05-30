package com.example.androidtermproject.viewmodel

import com.example.androidtermproject.adapter.DrawerAdapter
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityCalendarBinding
import com.example.androidtermproject.databinding.CalendarDrawerLayoutBinding
import com.example.androidtermproject.databinding.CalendarDrawerListBinding

class CalendarActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityCalendarBinding
    lateinit var drawerBinding: CalendarDrawerLayoutBinding
    lateinit var drawerListBinding: CalendarDrawerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed
        )
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

        drawerBinding.myProfileButton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
//
//
//        drawerListBinding.friendsProfileButton.setOnClickListener {
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
//        }

        drawerBinding.profileRecyclerView.layoutManager = LinearLayoutManager(this)
        drawerBinding.profileRecyclerView.adapter = DrawerAdapter(friendsProfile)

        drawerBinding.logoutButton.setOnClickListener {
            showLogoutDialog()
        }
        drawerBinding.withdrawButton.setOnClickListener {
            showWithdrawDialog()
        }
        val moveToDiaryButton = binding.root.findViewById<Button>(R.id.moveToDiaryButton)
        moveToDiaryButton.text = "Move to Diary"
        moveToDiaryButton.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("로그아웃 하시겠습니까?")
        builder.setPositiveButton("로그아웃") { dialog, _ ->
            // 로그아웃 로직을 여기에 추가
            // 예를 들어, 로그인 화면으로 이동하는 등의 동작을 수행할 수 있습니다.
            dialog.dismiss() // 다이얼로그 닫기
        }
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    private fun showWithdrawDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("탈퇴")

        // 다이얼로그에 여러 줄을 입력할 수 있는 EditText 추가
        val input = EditText(this)
        input.maxLines = 5 // 여러 줄 입력 가능하도록 설정
        input.hint = "탈퇴 사유 (100글자 제한)" // placeholder 설정
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("탈퇴") { dialog, _ ->
            val withdrawReason = input.text.toString()
            // 탈퇴 이유를 처리하는 로직을 여기에 추가
            // withdrawReason을 이용하여 탈퇴 처리를 수행할 수 있습니다.
            dialog.dismiss() // 다이얼로그 닫기
        }
        builder.setNegativeButton("취소", null)
        builder.show()
    }

    data class ProfileData(val img: Int, val name: String, val color: Int?)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
