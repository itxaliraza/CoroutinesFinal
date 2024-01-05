package com.practice.coroutines.ui.quotes.domain.model

import com.google.gson.annotations.SerializedName

data class QuoteItem(
    @SerializedName("a")
    val author: String,
    @SerializedName("h")
    val highlightedQuote: String,
    @SerializedName("q")
    val quote: String
)