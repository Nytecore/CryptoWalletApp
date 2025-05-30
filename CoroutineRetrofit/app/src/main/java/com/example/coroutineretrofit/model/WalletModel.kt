package com.example.coroutineretrofit.model

import com.google.firebase.Timestamp

data class WalletData(
    val timestamp: Timestamp? = null,
    val amount: Double = 0.0,
    val type: String = ""
)