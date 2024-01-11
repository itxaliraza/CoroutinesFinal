package com.practice.coroutines.quote_of_day

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.practice.coroutines.databinding.ActivityQuoteOfTheDayBinding
import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.quotes.single_quote.QuoteOfDayViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuoteOfTheDayActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityQuoteOfTheDayBinding
    private val quoteOfDayViewmodel: QuoteOfDayViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnTryAgain.setOnClickListener {
                quoteOfDayViewmodel.fetchQuoteOfDay()
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    quoteOfDayViewmodel.quoteOfDay.collectLatest {
                        when (it) {
                            is MyResult.Failure -> {
                                pbMain.makeGone()
                                clError.makeVisible()
                                tvError.text = it.message
                            }

                            MyResult.Idle -> {
                                pbMain.makeVisible()
                            }

                            MyResult.Loading -> {
                                pbMain.makeVisible()
                                clError.makeGone()
                            }

                            is MyResult.Success -> {
                                clError.makeGone()
                                pbMain.makeGone()
                                tvQuote.text = it.data.quote
                            }

                            null -> {

                            }
                        }
                    }
                }
            }
        }

    }
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}