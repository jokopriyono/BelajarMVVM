package com.jokopriyono.belajarmvvm.data.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("code")
    val code: String,
    @SerializedName("directLink")
    val directLink: String,
    @SerializedName("downloadPage")
    val downloadPage: String,
    @SerializedName("fileId")
    val fileId: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("info")
    val info: String,
    @SerializedName("md5")
    val md5: String,
    @SerializedName("parentFolder")
    val parentFolder: String
)