package com.example.unittests.characters.data

import com.example.unittests.characters.domain.FavoriteCharacter
import javax.inject.Inject

class FavoritesDomainMapper @Inject constructor() {
    fun fromEntity(entity: FavoriteEntity): FavoriteCharacter {
        return FavoriteCharacter(
            id = entity.id,
        )
    }

    fun toEntity(favoriteCharacter: FavoriteCharacter): FavoriteEntity {
        return FavoriteEntity(
            id = favoriteCharacter.id,
        )
    }
}
