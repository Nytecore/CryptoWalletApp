package com.example.coroutineretrofit.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.adapter.ActivityAdapter
import com.example.coroutineretrofit.databinding.FragmentActivityBinding
import com.example.coroutineretrofit.model.ActivityData
import com.example.coroutineretrofit.service.ActivityAPI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivityFragment : Fragment() {
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private val BASE_URL = "https://pro-api.coinmarketcap.com/v1/"
    private var activityModels: ArrayList<ActivityData>? = null
    private var recycleViewAdapter: ActivityAdapter? = null
    private var job: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadDate()


        binding.buttonRefresh.setOnClickListener {

            clickSound()
            loadDate()

        }



    }



    private fun loadDate() {

        binding.progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivityAPI::class.java)


        job = CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getCryptoListings()

            withContext(Dispatchers.Main + exceptionHandler) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {

                    response.body()?.let {it ->

                        activityModels = ArrayList(it.data)
                        recycleViewAdapter = ActivityAdapter(it.data)
                        binding.recyclerView.adapter = recycleViewAdapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }

                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer = null
        job?.cancel()

    }
}