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
import com.example.coroutineretrofit.databinding.FragmentResetPasswordBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetButton.setOnClickListener {

            val userName = binding.resetUsernameEditText.text.toString()
            val eMail = binding.resetEmailEditText.text.toString()

            if (userName.isNotEmpty() && eMail.isNotEmpty()) {

                Snackbar.make(
                    requireView(),
                    "If an account with this information exists, a verification email has been sent..",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        val action = ResetPasswordFragmentDirections.resetToLogin()
                        findNavController().navigate(action)
                        delay(500)
                        Toast.makeText(requireContext(), "E-mail sent!", Toast.LENGTH_SHORT).show()
                        resetSound()
                    }
                }.show()

            } else {
                Toast.makeText(requireContext(), "The fields is required and cannot be empty!", Toast.LENGTH_LONG).show()
                clickSound()
            }

        }

        binding.backResetToLogin.setOnClickListener {

            clickSound()
            val action = ResetPasswordFragmentDirections.resetToLogin()
            it.findNavController().navigate(action)
        }

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

    private fun resetSound() {

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.send_sound)
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