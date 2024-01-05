package com.practice.coroutines

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.coroutines.databinding.ActivityMainBinding
import com.practice.coroutines.quote_of_day.QuoteOfTheDayActivity
import com.practice.coroutines.ui.gallery.GalleryActivity
import com.practice.coroutines.ui.quotes.multiple_quote.PopularQuotesActivity
import com.practice.coroutines.ui.supervisor.SupervisorActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnMove.setOnClickListener {
                startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
            }
            btnFetchSingleQuote.setOnClickListener {
                startActivity(Intent(this@MainActivity, QuoteOfTheDayActivity::class.java))
            }
            btnQuotesList.setOnClickListener {
                startActivity(Intent(this@MainActivity, PopularQuotesActivity::class.java))
            }
            btnSupervisorJob.setOnClickListener {
                startActivity(Intent(this@MainActivity, SupervisorActivity::class.java))
            }
        }

    }
}