package com.example.androidtermproject.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidtermproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var commentBeforeEdit = ""
    private var contactBeforeEdit = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore

    private val PICK_IMAGE_REQUEST = 71
    private var currentUserId: String = ""
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""

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

    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 액티비티 종료
        return true
    }

    private fun loadProfileData() {
        // Load profile data from Firestore and display it in UI
        db.collection("users").document(currentUserId)
            .get()
            .addOnSuccessListener { document ->
                val profileName = document.getString("name") ?: ""
                val profileComment = document.getString("profileComment") ?: ""
                val profileContact = document.getString("profileContact") ?: ""
                val profileImageUrl = document.getString("profileImageUrl") ?: ""

                binding.profileName.text = profileName
                binding.profileComment.text = profileComment
                binding.profileContact.text = profileContact

                // Load profile image
                if (profileImageUrl.isNotEmpty()) {
                    Glide.with(this)
                        .load(profileImageUrl)
                        .into(binding.profileImageView)
                }

                switchEditMode(false)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load profile data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
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
                    saveProfile()
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

        binding.profileImageView.setOnClickListener {
            if (binding.profileRightButton.text == "Save") {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            imageUri = data.data
            binding.profileImageView.setImageURI(imageUri)
        }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        if (user != null) {
            if (imageUri != null) {
                val ref = storage.reference.child("profileImages/${user.uid}")
                val uploadTask = ref.putFile(imageUri!!)

                uploadTask.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        saveImageUriToFirestore(uri.toString())
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                saveProfileDetails(null)
            }
        }
    }

    private fun saveImageUriToFirestore(imageUrl: String) {
        val user = auth.currentUser
        if (user != null) {
            val userProfile = hashMapOf(
                "profileImageUrl" to imageUrl,
                "profileComment" to binding.profileCommentEdit.text.toString(),
                "profileContact" to binding.profileContactEdit.text.toString()
            )

            db.collection("users").document(user.uid)
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                    binding.profileComment.text = binding.profileCommentEdit.text
                    binding.profileContact.text = binding.profileContactEdit.text
                    commentBeforeEdit = binding.profileComment.text.toString()
                    contactBeforeEdit = binding.profileContact.text.toString()
                    switchEditMode(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveProfileDetails(imageUrl: String?) {
        val user = auth.currentUser
        if (user != null) {
            val userProfile = hashMapOf(
                "profileImageUrl" to (imageUrl ?: ""),
                "profileComment" to binding.profileCommentEdit.text.toString(),
                "profileContact" to binding.profileContactEdit.text.toString()
            )

            db.collection("users").document(user.uid)
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                    binding.profileComment.text = binding.profileCommentEdit.text
                    binding.profileContact.text = binding.profileContactEdit.text
                    commentBeforeEdit = binding.profileComment.text.toString()
                    contactBeforeEdit = binding.profileContact.text.toString()
                    switchEditMode(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
