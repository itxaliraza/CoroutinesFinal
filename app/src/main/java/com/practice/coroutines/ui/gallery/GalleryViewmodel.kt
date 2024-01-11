package com.practice.coroutines.ui.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.coroutines.domain.model.MyResult
import com.practice.coroutines.ui.gallery.data.PhotosPicker
import com.practice.coroutines.ui.gallery.data.VideosPicker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
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
            val jobsList = mutableListOf<Job>()
            val job1: Job = launch { photosPicker.queryPhotos() }
            val job2: Job = launch { videosPicker.queryVideos() }
            jobsList.add(job1)
            jobsList.add(job2)
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


    private fun cancelPrevFetching() {
        dataFetcherJob?.cancel()
    }


    private fun sendDataFetchingCompletedEvent() {
        // post event to UI or firebase
    }

    private fun showLoading() {
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