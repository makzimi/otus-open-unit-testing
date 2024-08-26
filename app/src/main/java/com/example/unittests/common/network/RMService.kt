package com.example.unittests.common.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.unittests.common.network.dto.RMResponseDto

interface RMService {
    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int = 0): RMResponseDto
}