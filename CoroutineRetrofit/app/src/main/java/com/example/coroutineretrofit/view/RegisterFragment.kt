package com.example.coroutineretrofit.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth? = null
    private lateinit var firestore: FirebaseFirestore
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {

            val userName = binding.usernameEditText.text.toString().trim()
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.resisterPasswordEditText.text.toString()
            val rePassword = binding.resisterPasswordEditText2.text.toString()

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_LONG)
                    .show()
                clickSound()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_LONG)
                    .show()
                clickSound()
                return@setOnClickListener
            }

            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnSuccessListener { authResult ->
                    val user = authResult.user
                    if (user != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()

                        user.updateProfile(profileUpdates)

                        val userData = mapOf(
                            "userName" to userName,
                            "email" to email,
                            "createdAt" to FieldValue.serverTimestamp()
                        )

                        firestore.collection("Users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Registered successfully!",
                                    Toast.LENGTH_LONG
                                ).show()
                                registerSound()
                                val action =
                                    RegisterFragmentDirections.actionRegisterFragmentToCryptoFragment()
                                view.findNavController().navigate(action)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Firestore error: ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
                ?.addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Auth error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        binding.signInTextView.setOnClickListener {
            val actionToLogin = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            it.findNavController().navigate(actionToLogin)
            clickSound()
        }

    }


    private fun clickSound() {

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext() , R.raw.standard_click)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    private fun registerSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext() , R.raw.register_sound)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer = null
    }
}


