package com.practice.coroutines.ui.gallery.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.gallery.model.MediaItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class VideosPicker @Inject constructor(@ApplicationContext private val context: Context) {

    private val _videosMutableFlow: MutableStateFlow<MyResult<List<MediaItem>>> =
        MutableStateFlow(MyResult.Idle)
    val videosPickerStateFlow = _videosMutableFlow.asStateFlow()
    private fun getProjections(): Array<String> {
        return arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )
    }

    private fun getContentUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    }


    suspend fun queryVideos() {
        _videosMutableFlow.value = MyResult.Loading
        val videosList: ArrayList<MediaItem> = ArrayList()
        withContext(Dispatchers.IO) {
            context.contentResolver.query(
                getContentUri(), getProjections(), null, null,
                MediaStore.Video.Media.DATE_ADDED
            )?.use {
                it.moveToFirst()
                val id = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val data =
                    it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val displayName =
                    it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val durationIndex =
                    it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

                while (!it.isAfterLast) {
                    val mDuration = it.getLong(durationIndex)

                    val mediaItem = MediaItem(
                        name = it.getString(displayName),
                        path = it.getString(data),
                        uri = ContentUris.withAppendedId(
                            getContentUri(), it.getLong(id)
                        ).toString(),
                        size = it.getLong(sizeIndex),
                        duration = mDuration.getFormattedDuration(),
                        durationInt = mDuration,
                    )
                    videosList.add(
                        mediaItem
                    )
                    it.moveToNext()
                }
            }

            _videosMutableFlow.value = MyResult.Success(videosList)
        }
    }

    fun Long.getFormattedDuration(): String {
        val seconds = (this / 1000) % 60
        val minutes = (this / (1000 * 60)) % 60
        val hours = (this / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
