package com.example.unittests.common.network.dto

import com.google.gson.annotations.SerializedName

data class RMResponseDto(
    @SerializedName("info") val info: ResponseDto,
    @SerializedName("results") val results: List<CharacterDto>,
    @SerializedName("error") val error: String?
)
