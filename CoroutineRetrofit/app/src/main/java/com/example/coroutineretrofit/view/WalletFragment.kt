package com.example.coroutineretrofit.view

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.adapter.WalletAdapter
import com.example.coroutineretrofit.databinding.FragmentWalletBinding
import com.example.coroutineretrofit.model.WalletData
import com.example.coroutineretrofit.service.WalletRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var job: Job? = null
    private var walletRepository = WalletRepository()
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTotalBalance(userId!!)

        binding.activityButton.setOnClickListener { vw ->

            val action = WalletFragmentDirections.actionWalletFragmentToActivityFragment()
            vw.findNavController().navigate(action)
            clickSound()

        }

        binding.transfersButton.setOnClickListener { vw ->

            val action = WalletFragmentDirections.actionWalletFragmentToTransfersFragment()
            vw.findNavController().navigate(action)
            clickSound()

        }

    }



    @SuppressLint("SetTextI18n")
    private fun getTotalBalance(userId: String) {


        job = CoroutineScope(Dispatchers.IO).launch {
            val transactions = walletRepository.getWalletTransactions(userId)

            withContext(Dispatchers.Main) {
                setupRecyclerView(transactions)
            }
        }


        firestore.collection("Users").document(userId)
            .get().addOnSuccessListener { data ->
                val databaseRef = firestore.collection("Users").document(userId)

                    // Has any data like "totalAmount" in database -->
                if (data.exists()) {
                    val totalAmount = data.getDouble("totalAmount")

                        // --> Show it in UI directly
                    if (totalAmount != null) {
                        binding.balanceTextViewWallet.text = formatAmount(totalAmount)

                    } else {
                            // --> If not, reset.
                        val totalAmountData = hashMapOf("totalAmount" to 0.0)
                        databaseRef.set(totalAmountData, SetOptions.merge())
                        binding.balanceTextViewWallet.text = "$0.00"
                    }
                } else {
                        // If there is no data (New user)
                    val totalAmountData = hashMapOf("totalAmount" to 0.0)
                    databaseRef.set(totalAmountData, SetOptions.merge())
                    binding.balanceTextViewWallet.text = "$0.00"
                }
            }
    }

    private fun formatAmount(amount: Double): String {
        return "$${"%.2f".format(amount)}"
    }

    private fun setupRecyclerView(transactions: List<WalletData>) {
        val adapter = WalletAdapter(transactions)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun clickSound() {

        mediaPlayer?.release()

        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.standard_click)
        mediaPlayer?.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer = null
    }
}