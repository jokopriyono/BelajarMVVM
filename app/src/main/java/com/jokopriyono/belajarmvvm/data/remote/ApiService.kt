package com.jokopriyono.belajarmvvm.data.remote

import com.jokopriyono.belajarmvvm.data.model.CharactersResponse
import com.jokopriyono.belajarmvvm.data.model.UploadResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("characters")
    suspend fun getAllDisneyCharacters(
    ): ApiResponse<CharactersResponse>

    @Multipart
    @POST
    suspend fun uploadFile(
        @Url url: String,
        @Part file: MultipartBody.Part,
        @Part("token") token: RequestBody
    ): ApiResponse<UploadResponse>

}