package com.example.coroutineretrofit.service

import com.example.coroutineretrofit.model.WalletData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class WalletRepository {

    suspend fun getWalletTransactions(userId: String): List<WalletData> {

        return try {
            val data = Firebase.firestore
                .collection("Users")
                .document(userId)
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            data.documents.mapNotNull { it.toObject(WalletData::class.java) }

        } catch (e: Exception) {
            emptyList()
        }
    }
}