package com.jokopriyono.belajarmvvm.data.model


import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)