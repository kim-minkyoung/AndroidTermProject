package com.example.androidtermproject.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidtermproject.R
import com.example.androidtermproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var nameBeforeEdit = ""
    private var commentBeforeEdit = ""
    private var contactBeforeEdit = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore

    private val PICK_IMAGE_REQUEST = 71
    private var currentUserId: String = ""
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
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

        // ActivityResultLauncher 초기화
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val data: Intent? = result.data
                if (data?.data != null) {
                    imageUri = data.data
                    binding.profileImageView.setImageURI(imageUri)
                    saveProfileDetails(imageUri.toString())
                } else {
                    Toast.makeText(this, "Error getting selected file", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        setupButtonListeners()

        supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        }

//        val profileName = "John Doe"
//        binding.profileName.text = profileName

        nameBeforeEdit = binding.profileName.text.toString()
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
            .collection("profile").document("profileData")
            .get()
            .addOnSuccessListener { document ->
                val profileName = document.getString("profileName") ?: "write your name"
                val profileComment = document.getString("profileComment") ?: "ex) I love listening music"
                val profileContact = document.getString("profileContact") ?: "ex) hongGilDong (instagram)"
                val profileImageUrl = document.getString("profileImageUrl") ?: "@drawable/default_profile"

                binding.profileName.text = profileName
                binding.profileComment.text = profileComment
                binding.profileContact.text = profileContact

                // Load profile image
                if (profileImageUrl.isNotEmpty() && profileImageUrl != "@drawable/default_profile") {
                    Glide.with(this)
                        .load(profileImageUrl)
                        .into(binding.profileImageView)
                } else {
                    Glide.with(this)
                        .load(R.drawable.default_profile)
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
            }
        }

        binding.profileImageView.setOnClickListener {
            if (binding.profileRightButton.text == "Save") {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            }
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
                        saveProfileDetails(uri.toString())
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                saveProfileDetails(null)
            }
        }
    }

    private fun saveProfileDetails(imageUrl: String?) {
        val user = auth.currentUser
        if (user != null) {
            val userProfile = mapOf(
                "profileImageUrl" to (imageUrl ?: ""),
                "profileName" to binding.profileNameEdit.text.toString(),
                "profileComment" to binding.profileCommentEdit.text.toString(),
                "profileContact" to binding.profileContactEdit.text.toString()
            )

            db.collection("users").document(user.uid)
                .collection("profile").document("profileData")
                .set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                    binding.profileName.text = binding.profileNameEdit.text
                    binding.profileComment.text = binding.profileCommentEdit.text
                    binding.profileContact.text = binding.profileContactEdit.text
                    nameBeforeEdit = binding.profileName.text.toString()
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
            binding.profileName.visibility = View.GONE
            binding.profileNameEdit.visibility = View.VISIBLE
            binding.profileComment.visibility = View.GONE
            binding.profileCommentEdit.visibility = View.VISIBLE
            binding.profileContact.visibility = View.GONE
            binding.profileContactEdit.visibility = View.VISIBLE

            binding.profileNameEdit.setText(binding.profileName.text)
            binding.profileCommentEdit.setText(binding.profileComment.text)
            binding.profileContactEdit.setText(binding.profileContact.text)

            binding.profileLeftButton.text = "Cancel"
            binding.profileRightButton.text = "Save"
        } else {
            binding.profileName.visibility = View.VISIBLE
            binding.profileNameEdit.visibility = View.GONE
            binding.profileComment.visibility = View.VISIBLE
            binding.profileCommentEdit.visibility = View.GONE
            binding.profileContact.visibility = View.VISIBLE
            binding.profileContactEdit.visibility = View.GONE

            binding.profileLeftButton.text = "Music Calendar"
            binding.profileRightButton.text = "Edit"
        }
    }
}
