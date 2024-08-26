package com.example.unittests.characters.data

import com.example.unittests.characters.domain.FavoriteCharacter
import com.example.unittests.characters.domain.FavoritesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesDataSource: FavoritesDataSource,
    private val domainMapper: FavoritesDomainMapper,
    private val dispatcher: CoroutineDispatcher,
): FavoritesRepository {
    override fun consumeFavorites(): Flow<List<FavoriteCharacter>> {
        return favoritesDataSource.consumeFavorites()
            .map { favoriteEntities ->
                favoriteEntities.map(domainMapper::fromEntity)
            }
            .flowOn(dispatcher)
    }

    override suspend fun addToFavorites(favorite: FavoriteCharacter) = withContext(dispatcher) {
        favoritesDataSource.saveFavorite(favorite.let(domainMapper::toEntity))
    }

    override suspend fun removeFromFavorites(favorite: FavoriteCharacter) = withContext(dispatcher) {
        favoritesDataSource.removeFavorite(favorite.let(domainMapper::toEntity))
    }
}