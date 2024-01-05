package com.practice.coroutines.ui.quotes.single_quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.coroutines.ui.quotes.domain.repo.SingleQuotesrepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteOfDayViewmodel @Inject constructor(private val quotesrepository: SingleQuotesrepository) :
    ViewModel() {
    fun fetchQuoteOfDay() {
        viewModelScope.launch {
            quotesrepository.fetchQuoteOfDayIfNotFetched()
        }
    }

    init {
        val quoteFetchingJob: Job = viewModelScope.launch {
            fetchQuoteOfDay()
        }
    }

    val quoteOfDay =
        quotesrepository.quoteOfDay.stateIn(viewModelScope, SharingStarted.Eagerly, null)

}