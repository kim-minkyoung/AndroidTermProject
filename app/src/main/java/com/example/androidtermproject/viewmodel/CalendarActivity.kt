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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
        builder.setTitle("회원 탈퇴 안내")
        builder.setMessage("이용해 주셔서 감사합니다. 회원 탈퇴는 영구적이며 복구할 수 없습니다. 저희 서비스를 이용해 주셨던 동안 진심으로 감사드립니다.")

        builder.setPositiveButton("탈퇴") { dialog, _ ->
            // Firebase 사용자 가져오기
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            // Firebase 사용자 삭제
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 사용자 삭제 성공
                    Toast.makeText(this, "탈퇴 처리 완료되었습니다.", Toast.LENGTH_SHORT).show()

                    // 로그아웃
                    FirebaseAuth.getInstance().signOut()

                    // 로그인 화면으로 이동
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 화면 종료
                } else {
                    // 사용자 삭제 실패
                    val errorMessage = "탈퇴 처리 실패: ${task.exception?.message}"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

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
