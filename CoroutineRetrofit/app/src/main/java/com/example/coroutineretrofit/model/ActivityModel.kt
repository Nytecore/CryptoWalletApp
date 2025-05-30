package com.example.coroutineretrofit.model

data class ActivityModel(
    val data: List<ActivityData>
)

data class ActivityData(
    val name: String,
    val symbol: String,
    val quote: QuoteMap
)

data class QuoteMap(
    val USD: Quote
)

data class Quote(
    val price: Float
)