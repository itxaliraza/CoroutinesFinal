package com.practice.coroutines.ui.quotes.multiple_quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.quotes.data.repo.MultipleQuotesRepositoryImpl
import com.practice.coroutines.ui.quotes.domain.model.QuotesList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularQuotesViewModel @Inject constructor(private val quotesrepository: MultipleQuotesRepositoryImpl) :
    ViewModel() {

    private val _quotesListMutable: MutableStateFlow<MyResult<QuotesList>?> = MutableStateFlow(null)
    val quotesList = _quotesListMutable.asStateFlow()

    fun fetchPopularQuotes() {
        _quotesListMutable.value = MyResult.Loading

        viewModelScope.launch {

            val quotesResult: Deferred<MyResult<QuotesList>> =
                async { quotesrepository.getQuotesList() }

            _quotesListMutable.value = quotesResult.await()
        }
    }

    init {
        fetchPopularQuotes()
    }

}