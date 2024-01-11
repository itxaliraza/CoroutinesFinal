package com.practice.coroutines.ui.quotes.domain.repo

import com.practice.coroutines.model.MyResult
 import com.practice.coroutines.ui.quotes.domain.model.QuoteItem
import kotlinx.coroutines.flow.StateFlow

interface SingleQuotesrepository {
    suspend fun fetchQuoteOfDayIfNotFetched()
    val quoteOfDay: StateFlow<MyResult<QuoteItem>>
}