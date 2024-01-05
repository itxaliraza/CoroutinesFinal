package com.practice.coroutines.ui.gallery.videoFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.practice.coroutines.databinding.ItemVideoBinding
import com.practice.coroutines.ui.gallery.model.MediaItem
import javax.inject.Inject

class VideoAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<MediaItem, VideoAdapter.ItemViewHolder>(DiffUtils) {
    inner class ItemViewHolder(val binding: ItemVideoBinding) : ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {
            with(binding) {
                glide.load(mediaItem.uri)
                    .into(ivPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object DiffUtils : DiffUtil.ItemCallback<MediaItem>() {
    override fun areContentsTheSame(
        oldItem: MediaItem, newItem: MediaItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: MediaItem, newItem: MediaItem
    ): Boolean {
        return oldItem.uri == newItem.uri
    }
}
