package com.example.unittests.characters.data

import com.example.unittests.common.network.dto.CharacterDto
import javax.inject.Inject

class CharacterDataMapper @Inject constructor() {
    fun toEntity(dto: CharacterDto): CharacterEntity {
        return CharacterEntity(
            id = dto.id.toString(),
            name = dto.name,
            image = dto.image,
        )
    }
}
