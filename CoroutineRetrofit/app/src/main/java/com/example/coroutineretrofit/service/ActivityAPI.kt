package com.example.coroutineretrofit.service

import com.example.coroutineretrofit.model.ActivityModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ActivityAPI {

    @Headers("X-CMC_PRO_API_KEY: ---> YOUR ")
    @GET("cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD"
    ): Response<ActivityModel>
}