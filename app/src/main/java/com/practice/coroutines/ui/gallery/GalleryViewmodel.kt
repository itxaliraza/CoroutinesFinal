package com.practice.coroutines.ui.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.coroutines.domain.model.MyResult
import com.practice.coroutines.ui.gallery.data.PhotosPicker
import com.practice.coroutines.ui.gallery.data.VideosPicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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


    private var dataFetcherJob: Job? = null

    private fun cancelPrevFetching() {
        dataFetcherJob?.cancel()
    }

    fun fetchGalleryItemsBadPractice() {
        Log.d("cvrr", "Data Fetching started")

        viewModelScope.launch {
            cancelPrevFetching()
            dataFetcherJob = launch {
                photosPicker.queryMedia()
                videosPicker.queryVideos()
            }
            updateUi()
            dataFetcherJob?.join()
            sendDataFetchingCompletedEvent()
            Log.d("cvrr", "Data Fetching completed")

        }
    }


    fun fetchGalleryItemsGoodPractice() {
        Log.d("cvrr", "Data Fetching started")

        viewModelScope.launch {
            cancelPrevFetching()

            dataFetcherJob = launch(coroutineContext) {
                launch { photosPicker.queryMedia() }
                launch { videosPicker.queryVideos() }
            }
            updateUi()
            dataFetcherJob?.join()
            sendDataFetchingCompletedEvent()
            Log.d("cvrr", "Data Fetching completed")

        }
    }


    private fun sendDataFetchingCompletedEvent() {
        // post event to UI or firebase
    }

    private fun updateUi() {
        //Show loading
    }

    init {

        viewModelScope.launch {
            //bad practice
            // fetchGalleryItemsBadPractice()

            //good one
            fetchGalleryItemsGoodPractice()
        }

    }
}