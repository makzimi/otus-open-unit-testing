package com.example.unittests.characters.data

import com.example.unittests.characters.domain.CharacterRepository
import com.example.unittests.characters.domain.Character
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val dataMapper: CharacterDataMapper,
    private val domainMapper: CharacterDomainMapper,
    private val dispatcher: CoroutineDispatcher,
): CharacterRepository {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun consumeProducts(): Flow<List<Character>> {
        scope.launch {
            val characters = characterRemoteDataSource.getCharacters()
            characterLocalDataSource.saveCharacters(
                characters.map(dataMapper::toEntity)
            )
        }
        return characterLocalDataSource
            .consumeCharacters()
            .map { characterEntities ->
                characterEntities.map(domainMapper::fromEntity)
            }
    }
}
