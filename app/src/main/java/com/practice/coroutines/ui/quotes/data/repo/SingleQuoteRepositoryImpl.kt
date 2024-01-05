package com.practice.coroutines.ui.quotes.data.repo

import com.practice.coroutines.domain.model.MyResult
import com.practice.coroutines.ui.quotes.data.api.QuotesApi
import com.practice.coroutines.ui.quotes.domain.model.QuoteItem
import com.practice.coroutines.ui.quotes.domain.repo.SingleQuotesrepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleQuoteRepositoryImpl @Inject constructor(private val quotesApi: QuotesApi) :
    SingleQuotesrepository {

    private val _quoteOfDay: MutableStateFlow<MyResult<QuoteItem>> = MutableStateFlow(MyResult.Idle)


    override val quoteOfDay: StateFlow<MyResult<QuoteItem>>
        get() = _quoteOfDay.asStateFlow()


    override suspend fun fetchQuoteOfDayIfNotFetched() {
        try {
            if (quoteOfDay.value.isDataFecthedOrFetching()) {
                return
            }
            _quoteOfDay.value = MyResult.Loading
            val response = quotesApi.getQuoteOfTheDay()
            _quoteOfDay.value = if (response?.isSuccessful == true) {
                response.body()?.let {
                    MyResult.Success(it[0])
                } ?: MyResult.Failure("Unknown Error")
            } else {
                MyResult.Failure(response?.message() ?: "Unknown Error")
            }
        } catch (e: Exception) {
            _quoteOfDay.value = MyResult.Failure(e.message ?: "Unknown Error")
        }
    }

}

fun MyResult<QuoteItem>.isDataFecthedOrFetching(): Boolean {
    return when (this) {
        is MyResult.Failure -> false
        MyResult.Idle -> false
        MyResult.Loading -> true
        is MyResult.Success -> true
    }
}