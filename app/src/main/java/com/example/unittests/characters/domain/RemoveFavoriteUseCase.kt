package com.example.unittests.characters.domain

import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(favoriteCharacter: FavoriteCharacter) {
        favoritesRepository.removeFromFavorites(favoriteCharacter)
    }
}
