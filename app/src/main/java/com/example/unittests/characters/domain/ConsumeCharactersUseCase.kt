package com.example.unittests.characters.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConsumeCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(): Flow<List<Character>> {
        return characterRepository.consumeProducts()
    }
}