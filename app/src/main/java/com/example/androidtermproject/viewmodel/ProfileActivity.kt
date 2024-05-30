package com.example.androidtermproject.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtermproject.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var commentBeforeEdit = ""
    private var contactBeforeEdit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()

        supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

        val profileName = "John Doe"
        binding.profileName.text = profileName

        commentBeforeEdit = binding.profileComment.text.toString()
        contactBeforeEdit = binding.profileContact.text.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }

    private fun setupButtonListeners() {
        binding.profileLeftButton.setOnClickListener {
            if (binding.profileComment.visibility == View.GONE) {
                switchEditMode(false)
            } else {
                val intent = Intent(this, CalendarActivity::class.java)
                intent.putExtra("FROM_PROFILE", true)
                startActivity(intent)
            }
        }

        binding.profileRightButton.setOnClickListener {
            when (binding.profileRightButton.text) {
                "Save" -> {
                    binding.profileComment.text = binding.profileCommentEdit.text
                    binding.profileContact.text = binding.profileContactEdit.text

                    commentBeforeEdit = binding.profileComment.text.toString()
                    contactBeforeEdit = binding.profileContact.text.toString()

                    switchEditMode(false)
                }
                "Edit" -> {
                    switchEditMode(true)
                }
                "Cancel" -> {
                    binding.profileCommentEdit.setText(commentBeforeEdit)
                    binding.profileContactEdit.setText(contactBeforeEdit)

                    switchEditMode(false)
                }
            }
        }
    }

    private fun switchEditMode(isEditMode: Boolean) {
        if (isEditMode) {
            binding.profileComment.visibility = View.GONE
            binding.profileCommentEdit.visibility = View.VISIBLE
            binding.profileContact.visibility = View.GONE
            binding.profileContactEdit.visibility = View.VISIBLE

            binding.profileCommentEdit.setText(binding.profileComment.text)
            binding.profileContactEdit.setText(binding.profileContact.text)

            binding.profileLeftButton.text = "Cancel"
            binding.profileRightButton.text = "Save"
        } else {
            binding.profileComment.visibility = View.VISIBLE
            binding.profileCommentEdit.visibility = View.GONE
            binding.profileContact.visibility = View.VISIBLE
            binding.profileContactEdit.visibility = View.GONE

            binding.profileLeftButton.text = "Music Calendar"
            binding.profileRightButton.text = "Edit"
        }
    }
}
