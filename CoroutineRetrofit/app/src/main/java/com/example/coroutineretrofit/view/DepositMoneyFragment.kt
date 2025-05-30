package com.example.coroutineretrofit.view

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.FragmentDepositMoneyBinding
import com.example.coroutineretrofit.model.TransactionType
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DepositMoneyFragment : Fragment() {
    private var _binding: FragmentDepositMoneyBinding? = null
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
        _binding = FragmentDepositMoneyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.depositProgressBar.visibility = View.GONE

        clickListeners()



    }

    private fun clickListeners() {

        binding.depositButton.setOnClickListener {

            if (binding.depositAmountText.text.isNotEmpty()) {

                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("DEPOSIT MONEY")
                    .setMessage("Do you confirm the amount?")
                    .setIcon(R.drawable.question_icon)

                    .setPositiveButton("CONFIRM") {dialog, i ->
                        clickSound()
                        CoroutineScope(Dispatchers.Main)
                            .launch {
                                binding.depositProgressBar.visibility = View.VISIBLE
                                delay(1000)
                                depositTransactions()
                                binding.depositProgressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), "Money deposited." , Toast.LENGTH_LONG).show()
                                val action = DepositMoneyFragmentDirections.actionDepositMoneyFragmentToTransfersFragment(TransactionType.DEPOSIT.name)
                                it.findNavController().navigate(action)
                            }
                    }

                    .setNegativeButton("CANCEL") {dialog, which ->
                        clickSound()
                    }
                    .create()
                alertDialog.show()

            } else {
                Snackbar.make(it, "Amount cannot be empty!", Snackbar.LENGTH_SHORT).show()
                clickSound()
            }

        }

        binding.backDepositToTransfers.setOnClickListener {

            clickSound()
            val action = DepositMoneyFragmentDirections.actionDepositMoneyFragmentToTransfersFragment(TransactionType.NONE.name)
            it.findNavController().navigate(action)

        }

    }

    private fun depositTransactions() {
        val amount = binding.depositAmountText.text.toString().toDouble()
        val depositList = hashMapOf("depositAmount" to amount)

        if (userId != null){

            firestore.collection("Users")
                .document(userId)
                .set(depositList, SetOptions.merge())

                val transaction = hashMapOf(
                    "type" to "deposit",
                    "amount" to amount,
                    "timestamp" to Timestamp.now()
                )
                    firestore.collection("Users")
                        .document(userId)
                        .collection("transactions")
                        .add(transaction)
                    .addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

    }

    private fun clickSound() {

        mediaPlayer?.release()
        val mediaPlayer = MediaPlayer.create(requireContext() , R.raw.standard_click)
        mediaPlayer.start()
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