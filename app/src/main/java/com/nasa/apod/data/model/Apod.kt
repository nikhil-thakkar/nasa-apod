package com.nasa.apod.data.model

import com.google.gson.annotations.SerializedName

data class Apod(
    val copyright: String,
    val title: String,
    val date: String,
    val explanation: String,
    @SerializedName("hdurl")
    val hdUrl: String,
    @SerializedName("media_type")
    val mediaType: String,
    val url: String
)