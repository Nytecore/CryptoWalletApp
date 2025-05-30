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
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.FragmentTransfersBinding
import com.example.coroutineretrofit.model.TransactionType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TransfersFragment : Fragment() {
    private var _binding: FragmentTransfersBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var firestore: FirebaseFirestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransfersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.goDepositButton.setOnClickListener {

            clickSound()
            val action = TransfersFragmentDirections.actionTransfersFragmentToDepositMoneyFragment()
            it.findNavController().navigate(action)

        }

        binding.goWithdrawButton.setOnClickListener {

            clickSound()
            val action = TransfersFragmentDirections.actionTransfersFragmentToWithdrawMoneyFragment()
            it.findNavController().navigate(action)

        }

        binding.walletButton.setOnClickListener {

            clickSound()
            val action = TransfersFragmentDirections.actionTransfersFragmentToWalletFragment()
            it.findNavController().navigate(action)

        }

        financialTransactions(userId!!)

    }

    @SuppressLint("SetTextI18n")
    private fun financialTransactions(userId: String) {

        arguments?.let { argument ->

            val infoString = TransfersFragmentArgs.fromBundle(argument).info

            val transactionType = try {
                TransactionType.valueOf(infoString)
            } catch (e: Exception) {
                TransactionType.NONE
            }

            when (transactionType) {
                TransactionType.DEPOSIT -> {
                    // Deposit Transactions
                    handleDeposit(userId)
                }

                TransactionType.WITHDRAW -> {
                    // Withdraw Transactions
                    handleWithdraw(userId)
                }

                TransactionType.NONE -> {
                    // Opening
                    showBalance(userId)
                }
            }
        }
    }

    private fun updateTotalAmount(userId: String , newAmount: Double) {

        val data = hashMapOf("totalAmount" to newAmount)
        firestore.collection("Users").document(userId).set(data, SetOptions.merge())

    }

    private fun resetDepositAmount(userId: String , depositAmount: Double) {

        val data = hashMapOf("depositAmount" to depositAmount)
        firestore.collection("Users").document(userId).set(data, SetOptions.merge())

    }

    private fun resetWithdrawAmount(userId: String , withdrawAmount: Double) {

        val data = hashMapOf("withdrawAmount" to withdrawAmount)
        firestore.collection("Users").document(userId).set(data, SetOptions.merge())

    }

    private fun handleDeposit(userId: String) {

        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { data ->
                val depositedAmount = data.getDouble("depositAmount")
                var totalAmount = data.getDouble("totalAmount")

                if (totalAmount != null && depositedAmount != null) {
                    totalAmount += depositedAmount
                    updateTotalAmount(userId, totalAmount)
                    resetDepositAmount(userId, 0.0)

                    binding.totalAmount.text = formatAmount(totalAmount)
                }
            } .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }

    private fun handleWithdraw(userId: String) {

        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { data ->
                val withdrawnAmount = data.getDouble("withdrawAmount")
                var totalAmount = data.getDouble("totalAmount")

                if (totalAmount != null && withdrawnAmount != null) {
                    totalAmount -= withdrawnAmount
                    updateTotalAmount(userId, totalAmount)
                    resetWithdrawAmount(userId, 0.0)

                    binding.totalAmount.text = formatAmount(totalAmount)
                }
            } .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }

    }

    @SuppressLint("SetTextI18n")
    private fun showBalance(userId: String) {

        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { data ->

                val databaseRef = firestore.collection("Users").document(userId)

                    // Has any data like "totalAmount" in database -->
                if (data.exists()) {
                    val totalAmount = data.getDouble("totalAmount")

                        // --> Show it in UI directly
                    if (totalAmount != null) {
                        binding.totalAmount.text = formatAmount(totalAmount)

                    } else {
                            // --> If not, reset.
                        val totalAmountData = hashMapOf("totalAmount" to 0.0)
                        databaseRef.set(totalAmountData, SetOptions.merge())
                        binding.totalAmount.text = "$0.00"
                    }
                } else {
                        // If there is no data (New user)
                    val totalAmountData = hashMapOf("totalAmount" to 0.0)
                    databaseRef.set(totalAmountData, SetOptions.merge())
                    binding.totalAmount.text = "$0.00"
                }
            }

    }

    private fun formatAmount(amount: Double): String {
        return "$${"%.2f".format(amount)}"
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