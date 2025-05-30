package com.example.coroutineretrofit.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners()





    }



    private fun clickSound() {

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.standard_click)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    private fun loginSound() {

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(requireContext() , R.raw.register_sound)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    private fun clickListeners() {

        binding.loginButton.setOnClickListener {


            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val action = LoginFragmentDirections.actionLoginFragmentToCryptoFragment()
                        findNavController().navigate(action)
                        loginSound()
                    }.addOnFailureListener{
                        Toast.makeText(requireContext() , it.localizedMessage , Toast.LENGTH_LONG).show()
                        clickSound()

                    }
            } else {
                if (email.equals("")) {
                    Toast.makeText(requireContext() , "E-mail cannout be empty!" , Toast.LENGTH_SHORT).show()
                    clickSound()
                } else if (password.equals("")) {
                    Toast.makeText(requireContext() , "Password cannout be empty!" , Toast.LENGTH_SHORT).show()
                    clickSound()
                } else {
                    Toast.makeText(requireContext() , "E-mail or password cannot be empty!" , Toast.LENGTH_SHORT).show()
                    clickSound()
                }
            }

        }

        binding.signUpTextView.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            it.findNavController().navigate(action)
            clickSound()
        }

        binding.resetTextView.setOnClickListener {
            val action = LoginFragmentDirections.loginToResetPassword()
            it.findNavController().navigate(action)
            clickSound()
        }

        binding.rememberTextView.setOnClickListener {
            val action = LoginFragmentDirections.loginToRemember()
            it.findNavController().navigate(action)
            clickSound()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer = null
    }
}