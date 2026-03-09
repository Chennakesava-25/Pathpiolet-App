package com.simats.pathpiolet.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.simats.pathpiolet.databinding.ActivityEditProfileBinding
import com.simats.pathpiolet.databinding.ItemEditFieldBinding
import com.simats.pathpiolet.utils.SessionManager
import com.simats.pathpiolet.api.RetrofitClient
import com.simats.pathpiolet.api.UpdateProfileRequest
import com.simats.pathpiolet.api.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.AlertDialog
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sessionManager: SessionManager
    private var tempImageUri: Uri? = null

    private val pickGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            showImagePreview(it)
            uploadImage(it)
        }
    }
    
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            tempImageUri?.let {
                showImagePreview(it)
                uploadImage(it)
            }
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        setupFields()
        setupButtons()
        setupProfilePicture()
    }

    private fun setupFields() {
        ItemEditFieldBinding.bind(binding.fieldFullName.root).apply {
            tvLabel.text = "Full Name"
            etInput.hint = "Enter your full name"
            etInput.setText(sessionManager.getUsername())
        }

        ItemEditFieldBinding.bind(binding.fieldPhone.root).apply {
            tvLabel.text = "Phone Number"
            etInput.hint = "Enter your phone number"
            etInput.setText(sessionManager.getPhone())
        }

        ItemEditFieldBinding.bind(binding.fieldAge.root).apply {
            tvLabel.text = "Age"
            etInput.hint = "Enter your age"
            etInput.setText(sessionManager.getAge().toString())
        }

        ItemEditFieldBinding.bind(binding.fieldEmail.root).apply {
            tvLabel.text = "Email (cannot be changed)"
            etInput.setText(sessionManager.getEmail())
            etInput.isEnabled = false
            etInput.setTextColor(android.graphics.Color.GRAY)
        }

        ItemEditFieldBinding.bind(binding.fieldEducation.root).apply {
            tvLabel.text = "Education Level"
            etInput.hint = "e.g. 12th MPC, B.Tech"
            etInput.setText(sessionManager.getEducation())
        }

        ItemEditFieldBinding.bind(binding.fieldInterested.root).apply {
            tvLabel.text = "Interested Field"
            etInput.hint = "e.g. Software, Core Engineering"
            etInput.setText(sessionManager.getInterested())
        }
    }

    private fun setupButtons() {
        binding.btnBack.root.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }
        
        val saveAction = {
            val fullName = ItemEditFieldBinding.bind(binding.fieldFullName.root).etInput.text.toString()
            val phone = ItemEditFieldBinding.bind(binding.fieldPhone.root).etInput.text.toString()
            val ageStr = ItemEditFieldBinding.bind(binding.fieldAge.root).etInput.text.toString()
            val age = ageStr.toIntOrNull() ?: 0
            val education = ItemEditFieldBinding.bind(binding.fieldEducation.root).etInput.text.toString()
            val interested = ItemEditFieldBinding.bind(binding.fieldInterested.root).etInput.text.toString()

            val userId = sessionManager.getUserId()
            if (userId != -1) {
                val request = UpdateProfileRequest(fullName, phone, age, education, interested)
                RetrofitClient.instance.updateProfile(userId, request).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            sessionManager.updateProfile(fullName, phone, age, education, interested)
                            Toast.makeText(this@EditProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EditProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Toast.makeText(this@EditProfileActivity, "Network error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Session error", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnSave.setOnClickListener { saveAction() }
        binding.btnSaveTop.setOnClickListener { saveAction() }
    }

    private fun setupProfilePicture() {
        val profileUrl = sessionManager.getProfilePicture()
        if (!profileUrl.isNullOrEmpty()) {
            binding.tvInitial.visibility = View.GONE
            binding.imgProfile.visibility = View.VISIBLE
            Glide.with(this)
                .load(RetrofitClient.BASE_URL.dropLast(1) + profileUrl)
                .into(binding.imgProfile)
        } else {
            binding.tvInitial.visibility = View.VISIBLE
            binding.imgProfile.visibility = View.GONE
            binding.tvInitial.text = sessionManager.getUsername().take(1).uppercase()
        }

        binding.btnChangePhoto.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery", "Remove Photo")
            AlertDialog.Builder(this)
                .setTitle("Update Profile Picture")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> checkCameraPermissionAndLaunch()
                        1 -> pickGalleryLauncher.launch("image/*")
                        2 -> removePhoto()
                    }
                }
                .show()
        }
    }

    private fun removePhoto() {
        val userId = sessionManager.getUserId()
        RetrofitClient.instance.deleteProfilePhoto(userId).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    sessionManager.saveProfilePicture("")
                    binding.imgProfile.visibility = View.GONE
                    binding.tvInitial.visibility = View.VISIBLE
                    binding.tvInitial.text = sessionManager.getUsername().take(1).uppercase()
                    Toast.makeText(this@EditProfileActivity, "Photo removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to remove photo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        try {
            tempImageUri = createTempImageUri()
            takePictureLauncher.launch(tempImageUri!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to launch camera: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTempImageUri(): Uri {
        val tempFile = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        if (!tempFile.exists()) {
            tempFile.createNewFile()
        }
        return FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", tempFile)
    }

    private fun showImagePreview(uri: Uri) {
        binding.tvInitial.visibility = View.GONE
        binding.imgProfile.visibility = View.VISIBLE
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.imgProfile)
    }

    private fun uploadImage(uri: Uri) {
        val file = File(cacheDir, "upload_image.jpg")
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

        val userId = sessionManager.getUserId()
        RetrofitClient.instance.uploadProfilePhoto(userId, body).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val url = response.body()?.profile_picture
                    if (!url.isNullOrEmpty()) {
                        sessionManager.saveProfilePicture(url)
                    }
                    Toast.makeText(this@EditProfileActivity, "Photo uploaded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Network error during upload", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
