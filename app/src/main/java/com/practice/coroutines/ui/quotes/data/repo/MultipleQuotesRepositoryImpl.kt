package com.practice.coroutines.ui.quotes.data.repo

import com.practice.coroutines.domain.model.MyResult
import com.practice.coroutines.ui.quotes.data.api.QuotesApi
import com.practice.coroutines.ui.quotes.domain.model.QuotesList
import com.practice.coroutines.ui.quotes.domain.repo.MultipleQuotesrepository
import javax.inject.Inject

class MultipleQuotesRepositoryImpl @Inject constructor(private val quotesApi: QuotesApi) :
    MultipleQuotesrepository {


    override suspend fun getQuotesList(): MyResult<QuotesList> {
        return try {
            val response = quotesApi.getQuotesList()
            if (response?.isSuccessful == true) {
                response.body()?.let {
                    MyResult.Success(it)
                } ?: MyResult.Failure("Unknown Error")
            } else {
                MyResult.Failure(response?.message() ?: "Unknown Error")
            }
        } catch (e: Exception) {
            MyResult.Failure(e.message ?: "Unknown Error")
        }
    }
}

