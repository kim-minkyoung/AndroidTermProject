package com.example.androidtermproject.viewmodel

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

        // 사용자의 프로필 정보를 받아올 수 있는 방법이 있다면 해당 정보를 가져와서 아래와 같이 설정할 수 있습니다.
        val profileName = "John Doe"
        // 이미지도 가져와서 설정할 수 있습니다.
        // val profileImage = ...

        binding.profileName.text = profileName
        // binding.profileImage.setImageDrawable(profileImage) // 이미지 설정 예시

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
                // Handle Music Calendar button click
            }
        }

        binding.profileRightButton.setOnClickListener {
            if (binding.profileRightButton.text == "Save") {
                // Save changes
                binding.profileComment.text = binding.profileCommentEdit.text
                binding.profileContact.text = binding.profileContactEdit.text

                commentBeforeEdit = binding.profileComment.text.toString()
                contactBeforeEdit = binding.profileContact.text.toString()

                switchEditMode(false)
            } else if (binding.profileRightButton.text == "Edit") {
                switchEditMode(true)
            } else if (binding.profileRightButton.text == "Cancel") {
                // Cancel changes
                binding.profileCommentEdit.setText(commentBeforeEdit)
                binding.profileContactEdit.setText(contactBeforeEdit)

                switchEditMode(false)
            }
        }
    }

    private fun switchEditMode(isEditMode: Boolean) {
        if (isEditMode) {
            // Switch to EditText
            binding.profileComment.visibility = View.GONE
            binding.profileCommentEdit.visibility = View.VISIBLE
            binding.profileContact.visibility = View.GONE
            binding.profileContactEdit.visibility = View.VISIBLE

            // Set EditText with current TextView content
            binding.profileCommentEdit.setText(binding.profileComment.text)
            binding.profileContactEdit.setText(binding.profileContact.text)

            binding.profileLeftButton.text = "Cancel"
            binding.profileRightButton.text = "Save"
        } else {
            // Switch to TextView
            binding.profileComment.visibility = View.VISIBLE
            binding.profileCommentEdit.visibility = View.GONE
            binding.profileContact.visibility = View.VISIBLE
            binding.profileContactEdit.visibility = View.GONE

            binding.profileLeftButton.text = "Music Calendar"
            binding.profileRightButton.text = "Edit"
        }
    }
}
