package com.practice.coroutines.ui.gallery.model

data class MediaItem(
    var name: String = "",
    var path: String = "",
    var uri: String = "",
    var size: Long = 0L,
    var duration: String = "",
    var durationInt: Long = 0L,
)