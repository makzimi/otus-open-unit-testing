package com.example.unittests.characters.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConsumeFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {
    operator fun invoke(): Flow<List<FavoriteCharacter>> {
        return favoritesRepository.consumeFavorites()
    }
}
