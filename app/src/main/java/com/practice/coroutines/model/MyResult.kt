package com.practice.coroutines.model

sealed class MyResult<out T> {
    data object Loading : MyResult<Nothing>()
    data object Idle : MyResult<Nothing>()
    data class Success<T>(val data: T) : MyResult<T>()
    data class Failure(val message: String) : MyResult<Nothing>()
}