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
import com.example.coroutineretrofit.databinding.FragmentWithdrawMoneyBinding
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
import kotlinx.coroutines.runBlocking

class WithdrawMoneyFragment : Fragment() {
    private var _binding: FragmentWithdrawMoneyBinding? = null
    private val binding get() = _binding!!
    private var mediaplayer: MediaPlayer? = null
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
        _binding = FragmentWithdrawMoneyBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.withdrawProgressBar.visibility = View.GONE
        clickListeners()








    }



    private fun withdrawTransactions() {

        if (userId != null) {
            val withdrawMoney = binding.withdrawAmountText.text.toString().toDouble()
            firestore.collection("Users").document(userId)
                .get().addOnSuccessListener {
                    val totalAmount = it.getDouble("totalAmount")

                    if (totalAmount != null && totalAmount > withdrawMoney) {
                        val withdrawAmount = hashMapOf("withdrawAmount" to withdrawMoney)
                        firestore.collection("Users").document(userId)
                            .set(withdrawAmount, SetOptions.merge())

                            val transaction = hashMapOf(
                                "type" to "withdraw",
                                "amount" to withdrawMoney,
                                "timestamp" to Timestamp.now()
                            )
                                firestore.collection("Users")
                                    .document(userId)
                                    .collection("transactions")
                                    .add(transaction)

                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Money withdrawn", Toast.LENGTH_LONG).show()
                                val action =
                                    WithdrawMoneyFragmentDirections.actionWithdrawMoneyFragmentToTransfersFragment(TransactionType.WITHDRAW.name)
                                view?.findNavController()?.navigate(action)
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        if (totalAmount == null || totalAmount < withdrawMoney) {
                            Toast.makeText(requireContext(), "Insufficient funds!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

    }

    private fun clickListeners() {

        binding.withdrawButton.setOnClickListener {

            if (binding.withdrawAmountText.text.isNotEmpty()) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("WITHDRAW MONEY")
                    .setMessage("Do you confirm the amount?")
                    .setIcon(R.drawable.question_icon)

                    .setPositiveButton("CONFIRM") { dialog, i ->

                        clickSound()
                        CoroutineScope(Dispatchers.Main)
                            .launch {

                                binding.withdrawProgressBar.visibility = View.VISIBLE
                                delay(1000)
                                withdrawTransactions()
                                binding.withdrawProgressBar.visibility = View.GONE

                            }
                    }

                    .setNegativeButton("CANCEL") {dialog, i ->
                        clickSound()
                    }.create()
                alertDialog.show()

            } else {
                Snackbar.make(it, "Amount cannot be empty!" , Snackbar.LENGTH_SHORT).show()
                clickSound()
            }

        }

        binding.backWithdrawToTransfers.setOnClickListener {

            clickSound()
            val action = WithdrawMoneyFragmentDirections.actionWithdrawMoneyFragmentToTransfersFragment(TransactionType.NONE.name)
            it.findNavController().navigate(action)

        }

    }

    private fun clickSound() {

        mediaplayer?.release()
        val mediaPlayer = MediaPlayer.create(requireContext() , R.raw.standard_click)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaplayer = null
    }
}