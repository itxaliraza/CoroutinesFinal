package com.practice.coroutines.ui.quotes.multiple_quote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.coroutines.databinding.ItemQuoteBinding
import com.practice.coroutines.ui.quotes.domain.model.QuoteItem
import javax.inject.Inject

class QuotesAdapter @Inject constructor() :
    ListAdapter<QuoteItem, QuotesAdapter.ItemViewHolder>(DiffUtils) {
    inner class ItemViewHolder(val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: QuoteItem) {
            with(binding) {
                tvQuote.text = mediaItem.quote
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemQuoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object DiffUtils : DiffUtil.ItemCallback<QuoteItem>() {
    override fun areContentsTheSame(
        oldItem: QuoteItem, newItem: QuoteItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: QuoteItem, newItem: QuoteItem
    ): Boolean {
        return oldItem.quote == newItem.quote
    }
}
