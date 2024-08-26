package com.example.unittests.characters.data

import com.example.unittests.characters.domain.Character
import javax.inject.Inject

class CharacterDomainMapper @Inject constructor() {
    fun fromEntity(entity: CharacterEntity): Character {
        return Character(
            id = entity.id,
            name = entity.name,
            image = entity.image,
        )
    }

    fun toEntity(character: Character): CharacterEntity {
        return CharacterEntity(
            id = character.id,
            name = character.name,
            image = character.image,
        )
    }
}