package com.example.coroutineretrofit.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutineretrofit.databinding.WalletRecyclerRowBinding
import com.example.coroutineretrofit.model.WalletData
import java.text.SimpleDateFormat
import java.util.Locale

class WalletAdapter(private val activityList: List<WalletData>): RecyclerView.Adapter<WalletAdapter.WalletHolder>() {

    class WalletHolder(val binding: WalletRecyclerRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletHolder {
        val binding = WalletRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletHolder(binding)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WalletHolder, position: Int) {

        val walletData = activityList[position]

            // Timestamp to formatted date string
        val formattedDate = walletData.timestamp?.toDate()?.let { date ->
            SimpleDateFormat("dd MM yyyy - HH:mm", Locale.getDefault()).format(date)
        }?: "No Data"


            // Amount value like: $250.00
        val amount = "$${"%.2f".format(walletData.amount)}"

        holder.binding.timeStampText.text = formattedDate

        if (walletData.type == "deposit") {
            holder.binding.amountTextView.setTextColor(Color.GREEN)
            holder.binding.amountTextView.text = "+$${"%.2f".format(walletData.amount)}"
        } else if (walletData.type == "withdraw"){
            holder.binding.amountTextView.setTextColor(Color.RED)
            holder.binding.amountTextView.text = "-$${"%.2f".format(walletData.amount)}"
        }

    }
}