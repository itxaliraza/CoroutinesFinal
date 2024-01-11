package com.practice.coroutines.ui.quotes.domain.repo

import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.quotes.domain.model.QuotesList

interface MultipleQuotesrepository {
    suspend fun getQuotesList(): MyResult<QuotesList>
}