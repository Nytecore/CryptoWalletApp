package com.example.coroutineretrofit.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import com.example.coroutineretrofit.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.coroutineretrofit.databinding.FragmentCryptoBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CryptoFragment : Fragment() {
    private var _binding: FragmentCryptoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var latestActivityJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCryptoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getUserData()

        latestActivity(userId!!)

        currentBalance(userId)

        clickListeners(view)


    }

    @SuppressLint("SetTextI18n")
    private fun latestActivity(userId: String) {
        latestActivityJob?.cancel()

        latestActivityJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val snapshot = firestore.collection("Users")
                    .document(userId)
                    .collection("transactions")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val document = snapshot.documents.firstOrNull()

                withContext(Dispatchers.Main) {
                    document?.let {
                        val type = it.getString("type")
                        val amount = it.getDouble("amount")

                        when (type) {
                            "deposit" -> {
                                binding.latestActivityTextView.setTextColor(Color.GREEN)
                                binding.latestActivityTextView.text = "+ $${amount}"
                            }

                            "withdraw" -> {
                                binding.latestActivityTextView.setTextColor(Color.RED)
                                binding.latestActivityTextView.text = "- $${amount}"
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun currentBalance(userId: String) {

        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { data ->

                val databaseRef = firestore.collection("Users").document(userId)

                if (data.exists()) {
                    val totalAmount = data.getDouble("totalAmount")
                    totalAmount?.let {
                        binding.currentBalanceText.text = formatAmount(totalAmount)
                    } ?: {
                        val totalAmountData = hashMapOf("totalAmount" to 0.0)
                        databaseRef.set(totalAmountData, SetOptions.merge())
                            .addOnFailureListener { exceptioon ->
                                Toast.makeText(requireContext(), exceptioon.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        binding.currentBalanceText.text = "$0.00"
                    }
                } else {
                    val totalAmountData = hashMapOf("totalAmount" to 0.0)
                    databaseRef.set(totalAmountData, SetOptions.merge())
                        .addOnFailureListener { exceptioon ->
                            Toast.makeText(requireContext(), exceptioon.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    binding.currentBalanceText.text = "$0.00"
                }

            }
    }

    private fun formatAmount(amount: Double): String {
        return "$${"%.2f".format(amount)}"
    }

    private fun clickListeners(view: View) {

            // View all text
        binding.viewAllText.setOnClickListener { navigateToWallet(view) }

            // View all text2
        binding.viewAllText2.setOnClickListener { navigateToWallet(view) }

            // Add button (FAB)
        binding.addButton.setOnClickListener { navigateToTransfers(view) }

            // Wallet constraint layout
        binding.walletConstraint.setOnClickListener { navigateToWallet(view) }

            // Transfer constraint layout
        binding.transfersConstraint.setOnClickListener { navigateToTransfers(view) }

            // Withdraw constraint layout
        binding.withdrawConstraint.setOnClickListener { navigateToWithdraw(view) }

            // Earn constraint layout
        binding.earnConstraint.setOnClickListener { navigateToActivity(view) }

            // Profile picture ImageView
        binding.profilPicrureImageView.setOnClickListener { navigateToProfile(view) }
    }

    private fun navigateToWallet(view: View) {
        clickSound()
        val action = CryptoFragmentDirections.actionCryptoFragmentToWalletFragment()
        view.findNavController().navigate(action)
    }

    private fun navigateToActivity(view: View) {
        clickSound()
        val action = CryptoFragmentDirections.actionCryptoFragmentToActivityFragment()
        view.findNavController().navigate(action)
    }

    private fun navigateToWithdraw(view: View) {
        clickSound()
        val action = CryptoFragmentDirections.actionCryptoFragmentToWithdrawMoneyFragment()
        view.findNavController().navigate(action)
    }

    private fun navigateToTransfers(view: View) {
        clickSound()
        val action = CryptoFragmentDirections.actionCryptoFragmentToTransfersFragment()
        view.findNavController().navigate(action)
    }

    private fun navigateToProfile(view: View) {
        clickSound()
        val action = CryptoFragmentDirections.actionCryptoFragmentToProfileFragment()
        view.findNavController().navigate(action)
    }

    private fun getUserPp () {

        firestore.collection("Users").document(userId!!)
            .get()
            .addOnSuccessListener { data ->

                val imageUrl = data.getString("downloadPpUrl")
                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.user_icon)
                        .error(R.drawable.user_icon)
                        .circleCrop()
                        .into(binding.profilPicrureImageView)
                } else {
                    binding.profilPicrureImageView.setImageResource(R.drawable.user_icon)
                }



            }
            .addOnFailureListener { ex -> Toast.makeText(requireContext(), "Photo error: ${ex.localizedMessage}", Toast.LENGTH_SHORT).show() }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        getUserPp()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser?.displayName
        binding.helloTextView.text = "Hello, $userName"
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
    }
}