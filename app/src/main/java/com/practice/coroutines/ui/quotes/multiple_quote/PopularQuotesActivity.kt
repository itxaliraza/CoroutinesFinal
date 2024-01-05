package com.practice.coroutines.ui.quotes.multiple_quote

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.coroutines.databinding.ActivityMultipleQuotesBinding
import com.practice.coroutines.domain.model.MyResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PopularQuotesActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityMultipleQuotesBinding
    private val popularQuotesViewModel: PopularQuotesViewModel by viewModels()
    lateinit var quotesAdapter: QuotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnTryAgain.setOnClickListener {
                popularQuotesViewModel.fetchPopularQuotes()
            }
            quotesAdapter = QuotesAdapter()
            rvQuotes.apply {
                layoutManager = LinearLayoutManager(this@PopularQuotesActivity)
                adapter = quotesAdapter
            }

            lifecycleScope.launch {
                popularQuotesViewModel.quotesList.flowWithLifecycle(
                    lifecycle,
                    Lifecycle.State.STARTED
                ).collectLatest {
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
                            quotesAdapter.submitList(it.data)
                        }

                        null -> {}
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