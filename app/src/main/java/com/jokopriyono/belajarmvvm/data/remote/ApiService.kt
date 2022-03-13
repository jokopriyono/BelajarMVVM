package com.jokopriyono.belajarmvvm.data.remote

import com.jokopriyono.belajarmvvm.data.model.CharactersResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("characters")
    suspend fun getAllDisneyCharacters(): ApiResponse<CharactersResponse>

}