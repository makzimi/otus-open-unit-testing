package com.example.unittests.characters.domain

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun consumeFavorites(): Flow<List<FavoriteCharacter>>
    suspend fun addToFavorites(favorite: FavoriteCharacter)
    suspend fun removeFromFavorites(favorite: FavoriteCharacter)
}