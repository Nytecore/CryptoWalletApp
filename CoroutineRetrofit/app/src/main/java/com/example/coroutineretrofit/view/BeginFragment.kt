package com.example.coroutineretrofit.view

import com.example.coroutineretrofit.R
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.coroutineretrofit.databinding.FragmentBeginBinding

class BeginFragment : Fragment() {
    private var _binding: FragmentBeginBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBeginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startButton.setOnClickListener {

            beginSound()
            val action = BeginFragmentDirections.actionBeginFragmentToLoginFragment()
            it.findNavController().navigate(action)



        }



    }

    private fun beginSound() {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.begin_sound)
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