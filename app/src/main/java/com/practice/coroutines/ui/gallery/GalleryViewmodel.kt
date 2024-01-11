package com.practice.coroutines.ui.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.gallery.data.PhotosPicker
import com.practice.coroutines.ui.gallery.data.VideosPicker
import com.practice.coroutines.ui.gallery.model.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewmodel @Inject constructor(
    private val photosPicker: PhotosPicker, private val videosPicker: VideosPicker
) : ViewModel() {

    val photosState = photosPicker.photosPickerStateFlow.stateIn(
        viewModelScope, SharingStarted.Eagerly, MyResult.Idle
    )
    val videosState = videosPicker.videosPickerStateFlow.stateIn(
        viewModelScope, SharingStarted.Eagerly, MyResult.Idle
    )

    private val _counterPhotoVideos: MutableStateFlow<Pair<Int?, Int?>> =
        MutableStateFlow(Pair(null, null))
    val counterPhotoVideos = _counterPhotoVideos.asStateFlow()


    private var dataFetcherJob: Job? = null

    fun fetchGalleryItems() {
        cancelPrevFetching()
        showLoading()
        dataFetcherJob = viewModelScope.launch {
            Log.d("cvrr", "Data Fetching started")
            photosPicker.queryPhotos()
            videosPicker.queryVideos()
            sendDataFetchingCompletedEvent()
            Log.d("cvrr", "Data Fetching completed")
        }
    }

    fun fetchGalleryItemsGoodPractice() {

        cancelPrevFetching()
        showLoading()
        dataFetcherJob = viewModelScope.launch {
            Log.d("cvrr", "Data Fetching started")
            val job1: Job = launch { photosPicker.queryPhotos() }
            val job2: Job = launch { videosPicker.queryVideos() }
            val jobsList: List<Job> = listOf(job1, job2)
            jobsList.joinAll()
            sendDataFetchingCompletedEvent()
            Log.d("cvrr", "Data Fetching completed")

        }
    }


    fun fetchGalleryItemsParallel() {
        cancelPrevFetching()
        showLoading()
        dataFetcherJob = viewModelScope.launch {
            Log.d("cvrr", "Data Fetching started")
            launch { photosPicker.queryPhotos() }
            launch { videosPicker.queryVideos() }
            sendDataFetchingCompletedEvent()
            Log.d("cvrr", "Data Fetching completed")
        }
    }


    fun fetchPhotosBadPractice(callback: (List<MediaItem>) -> Unit) {
        photosPicker.queryPhotosBadPractice(callback)

    }


    private fun cancelPrevFetching() {
        dataFetcherJob?.cancel()
    }


    private fun sendDataFetchingCompletedEvent() {
        if (photosState.value is MyResult.Success && videosState.value is MyResult.Success) {
            _counterPhotoVideos.value = Pair(
                (photosState.value as MyResult.Success<List<MediaItem>>).data.size,
                (videosState.value as MyResult.Success<List<MediaItem>>).data.size
            )
        }
    }

    private fun showLoading() {
        //Show loading
        _counterPhotoVideos.value = Pair(null, null)
    }

    init {

        viewModelScope.launch {
          photosPicker.queryPhotos()
        }

    }
}