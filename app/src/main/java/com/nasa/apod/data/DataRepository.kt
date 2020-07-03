package com.nasa.apod.data

import com.nasa.apod.data.model.Apod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.lang.Throwable

interface DataRepository {

    suspend fun getTodayPhoto(): Result<Apod>

    suspend fun getPhotoByDate(date: String): Result<Apod>
}

interface API {

    @GET("/planetary/apod")
    suspend fun getPhoto(@Query("date") date: String? = null): Apod
}

class RemoteRepository(private val api: API) : DataRepository {

    override suspend fun getTodayPhoto(): Result<Apod> {
        return try {
            Result.Success(withContext(Dispatchers.IO) {
                api.getPhoto()
            })
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }

    override suspend fun getPhotoByDate(date: String): Result<Apod> {
        return try {
            Result.Success(withContext(Dispatchers.IO) {
                api.getPhoto(date)
            })
        } catch (ex: Exception) {
            when (ex) {
                is HttpException, is IOException -> {
                    Result.Failure(ex)
                }
                else -> {
                    Result.Failure(ex)
                }
            }
        }
    }
}