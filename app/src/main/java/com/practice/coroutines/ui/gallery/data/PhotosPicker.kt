package com.practice.coroutines.ui.gallery.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.practice.coroutines.domain.model.MyResult
import com.practice.coroutines.ui.gallery.model.MediaItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosPicker @Inject constructor(@ApplicationContext private val context: Context) {

    private val _photosMutableFlow: MutableStateFlow<MyResult<List<MediaItem>>> =
        MutableStateFlow(MyResult.Idle)
    val photosPickerStateFlow = _photosMutableFlow.asStateFlow()

    private fun getPhotoProjections(): Array<String> {
        return arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
    }

    private fun getContentUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }


    suspend fun queryPhotos() {
        _photosMutableFlow.value = MyResult.Loading
        val photosList: ArrayList<MediaItem> = ArrayList()
        withContext(Dispatchers.IO) {
            context.contentResolver.query(
                getContentUri(), getPhotoProjections(), null, null, null
            )?.use { cursor ->
                cursor.moveToFirst()
                val data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                while (!cursor.isAfterLast) {
                    val mediaItem = MediaItem(
                        name = cursor.getString(displayName),
                        path = cursor.getString(data),
                        uri = ContentUris.withAppendedId(
                            getContentUri(), cursor.getLong(id)
                        ).toString(),
                        size = cursor.getLong(sizeIndex),
                        duration = "",
                        durationInt = 0L,
                    )
                    photosList.add(
                        mediaItem
                    )

                    cursor.moveToNext()
                }
            }
            _photosMutableFlow.value = MyResult.Success(photosList)
        }
    }


    fun queryPhotosBadPractice(callback: (List<MediaItem>) -> Unit) {
        val photosList: ArrayList<MediaItem> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch() {
            context.contentResolver.query(
                getContentUri(), getPhotoProjections(), null, null, null
            )?.use { cursor ->
                cursor.moveToFirst()
                val data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                while (!cursor.isAfterLast) {
                    val mediaItem = MediaItem(
                        name = cursor.getString(displayName),
                        path = cursor.getString(data),
                        uri = ContentUris.withAppendedId(
                            getContentUri(), cursor.getLong(id)
                        ).toString(),
                        size = cursor.getLong(sizeIndex),
                        duration = "",
                        durationInt = 0L,
                    )
                    photosList.add(
                        mediaItem
                    )

                    cursor.moveToNext()
                }

                callback(photosList)
            }
        }
    }

}


