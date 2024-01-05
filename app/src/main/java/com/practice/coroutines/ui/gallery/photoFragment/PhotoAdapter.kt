package com.practice.coroutines.ui.gallery.photoFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.practice.coroutines.databinding.ItemPhotoBinding
import com.practice.coroutines.ui.gallery.model.MediaItem
import javax.inject.Inject

class PhotoAdapter @Inject constructor(
    private val glide: RequestManager
) : ListAdapter<MediaItem, PhotoAdapter.ItemViewHolder>(DiffUtils) {
    inner class ItemViewHolder(val binding: ItemPhotoBinding) : ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {
            with(binding) {
                glide.load(mediaItem.uri)
                    .into(ivPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemPhotoBinding.inflate(
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
