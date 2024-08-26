package com.example.unittests.characters.data

import com.example.unittests.common.network.RMService
import com.example.unittests.common.network.dto.CharacterDto

interface CharacterRemoteDataSource {
    suspend fun getCharacters(): List<CharacterDto>
}

class CharacterRemoteDataSourceImpl (
    private val rmService: RMService,
): CharacterRemoteDataSource {
    override suspend fun getCharacters(): List<CharacterDto> {
        return rmService.getCharacters().results
    }
}
