package com.example.unittests.characters.presentation

import com.example.unittests.characters.domain.Character
import com.example.unittests.characters.domain.FavoriteCharacter
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class StateFactory @Inject constructor() {
    fun create(
        character: Character,
        favorites: Set<FavoriteCharacter>,
    ): CharacterState {
        return CharacterState(
            id = character.id,
            name = character.name,
            image = character.image,
            isFavorite = favorites.map(FavoriteCharacter::id).contains(character.id)
        )
    }
}