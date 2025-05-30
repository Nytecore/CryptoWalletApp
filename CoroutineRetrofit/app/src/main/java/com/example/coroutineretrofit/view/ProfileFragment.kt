package com.example.coroutineretrofit.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Intent>
    private var selectedPicture: Uri? = null

    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val userId get() = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        firestore = Firebase.firestore
        storage = Firebase.storage
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId?.let { getUserData(it) }
        setDefaultUI()
        binding.emailText.setText(auth.currentUser?.email)

        binding.editTextDate.setOnClickListener { showDatePickerDialog() }
        binding.imageProfile.setOnClickListener { handleImageSelection(view) }
        binding.editProfileButton.setOnClickListener { enableEditing(true) }
        binding.approveButton.setOnClickListener { saveProfile() }
    }

    private fun setDefaultUI() {
        binding.apply {
            nameEditText.isEnabled = false
            editTextDate.isEnabled = false
            imageProfile.isEnabled = false
            emailText.isEnabled = false
        }
    }

    private fun enableEditing(enable: Boolean) {
        binding.apply {
            nameEditText.isEnabled = enable
            editTextDate.isEnabled = enable
            imageProfile.isEnabled = enable
            approveButton.visibility = if (enable) View.VISIBLE else View.GONE
            editProfileButton.visibility = if (enable) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                binding.editTextDate.setText("${month + 1} / $day / $year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setButton(DialogInterface.BUTTON_POSITIVE, "SET", this)
            setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", this)
            show()
        }
    }

    private fun handleImageSelection(view: View) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> openGallery()
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission) -> {
                Snackbar.make(view, "Permission needed for gallery!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission") {
                        permissionLauncher.launch(permission)
                    }.show()
            }
            else -> permissionLauncher.launch(permission)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }

    private fun saveProfile() {
        binding.profileProgressBar.visibility = View.VISIBLE

        if (selectedPicture == null) {
            saveUserDataToFirestore(null)
        } else {
            val imageName = "${UUID.randomUUID()}.jpg"
            val imageRef = storage.reference.child("ProfilePictures/$imageName")

            imageRef.putFile(selectedPicture!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveUserDataToFirestore(uri.toString())
                    }
                }
                .addOnFailureListener { ex ->
                    showToast(ex.localizedMessage)
                    binding.profileProgressBar.visibility = View.GONE
                }
        }
    }

    private fun saveUserDataToFirestore(downloadUrl: String?) {
        val profileData = hashMapOf<String, Any?>(
            "userEmail" to auth.currentUser?.email,
            "usersName" to binding.nameEditText.text.toString(),
            "dateOfBirth" to binding.editTextDate.text.toString(),
            "downloadPpUrl" to downloadUrl
        )

        firestore.collection("Users").document(userId!!)
            .set(profileData, SetOptions.merge())
            .addOnSuccessListener {
                showToast("Saved successfully.")
                binding.profileProgressBar.visibility = View.GONE
                enableEditing(false)
            }
            .addOnFailureListener {
                showToast(it.localizedMessage)
                binding.profileProgressBar.visibility = View.GONE
            }
    }

    private fun getUserData(userId: String) {
        firestore.collection("Users").document(userId)
            .get()
            .addOnSuccessListener { data ->
                binding.nameEditText.setText(data.getString("usersName") ?: "")
                binding.editTextDate.setText(data.getString("dateOfBirth") ?: "")



                val imageUrl = data.getString("downloadPpUrl")
                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.user_icon)
                        .error(R.drawable.user_icon)
                        .circleCrop()
                        .into(binding.imageProfile)
                } else {
                    binding.imageProfile.setImageResource(R.drawable.user_icon)
                }



            }
            .addOnFailureListener { ex -> showToast(ex.localizedMessage) }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val selectedImageUri = result.data?.data
                    selectedImageUri?.let { uri ->
                        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "${UUID.randomUUID()}.jpg"))
                        val uCrop = UCrop.of(uri, destinationUri)
                            .withAspectRatio(1f, 1f)
                            .withOptions(getUCropOptions())

                        cropActivityResultLauncher.launch(uCrop.getIntent(requireContext()))
                    }
                }
            }

        cropActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                    val resultUri = UCrop.getOutput(result.data!!)
                    resultUri?.let {
                        selectedPicture = it
                        Glide.with(requireContext())
                            .load(it)
                            .placeholder(R.drawable.user_icon)
                            .error(R.drawable.user_icon)
                            .circleCrop()
                            .into(binding.imageProfile)
                    }
                } else if (result.resultCode == UCrop.RESULT_ERROR) {
                    val cropError = UCrop.getError(result.data!!)
                    showToast("Cropping failed: ${cropError?.localizedMessage}")
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    openGallery()
                } else {
                    showToast("Permission needed!")
                }
            }
    }

    private fun getUCropOptions(): UCrop.Options {
        return UCrop.Options().apply {
            setCircleDimmedLayer(true)
            setShowCropFrame(false)
            setShowCropGrid(false)
            setToolbarTitle("Fotoğrafı Kırp")
            setToolbarColor(ContextCompat.getColor(requireContext(), R.color.custom_color_2))
            setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.custom_color_2))
            setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(), R.color.custom_color_1))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}