package com.example.unittests.characters.domain

import kotlinx.coroutines.flow.Flow
import com.example.unittests.characters.domain.Character

interface CharacterRepository {
    fun consumeProducts(): Flow<List<Character>>
}