package com.example.coroutineretrofit.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutineretrofit.databinding.ActivityRecyclerRowBinding
import com.example.coroutineretrofit.model.ActivityData

class ActivityAdapter(val cryptoList: List<ActivityData>): RecyclerView.Adapter<ActivityAdapter.ActivityHolder>() {

    class ActivityHolder(val binding: ActivityRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
        val binding = ActivityRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ActivityHolder(binding)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
        holder.binding.recycleViewSymbolText.text = cryptoList[position].symbol
        holder.binding.recycleViewPriceText.text = "$${cryptoList[position].quote.USD.price}"
    }
}