package com.nasa.apod.data

sealed class Result<out T> {

    data class Success<out T : Any>(val data: T?) : Result<T>()

    data class Failure(val error: Throwable?) : Result<Nothing>()
}