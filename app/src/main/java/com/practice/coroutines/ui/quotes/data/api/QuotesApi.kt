package com.practice.coroutines.ui.quotes.data.api

import com.practice.coroutines.ui.quotes.domain.model.QuotesList
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("today")
    suspend fun getQuoteOfTheDay(): Response<QuotesList>?

    @GET("quotes")
    suspend fun getQuotesList(): Response<QuotesList>?

}