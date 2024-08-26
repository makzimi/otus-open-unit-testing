package com.example.unittests.characters.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

interface CharacterLocalDataSource {
    fun consumeCharacters(): Flow<List<CharacterEntity>>
    suspend fun saveCharacters(characters: List<CharacterEntity>)
}

class CharacterLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : CharacterLocalDataSource {
    override fun consumeCharacters(): Flow<List<CharacterEntity>> = dataStore.data
        .map(::mapCharactersFromPrefs)

    override suspend fun saveCharacters(characters: List<CharacterEntity>) {
        dataStore.edit { prefs -> prefs[charactersPreferencesKey] = encodeToString(characters) }
    }

    @OptIn(InternalSerializationApi::class)
    private fun decodeFromString(string: String): List<CharacterEntity> =
        try {
            Json.decodeFromString(ListSerializer(CharacterEntity::class.serializer()), string)
        } catch (e: Exception) {
            listOf()
        }

    private fun mapCharactersFromPrefs(prefs: Preferences): List<CharacterEntity> =
        prefs[charactersPreferencesKey]
            ?.takeIf(String::isNotEmpty)
            ?.let(this::decodeFromString) ?: listOf()

    private val charactersPreferencesKey = stringPreferencesKey(CHARACTERS_KEY)

    @OptIn(InternalSerializationApi::class)
    private fun encodeToString(products: List<CharacterEntity>): String =
        Json.encodeToString(
            ListSerializer(CharacterEntity::class.serializer()),
            products,
        )

    private companion object {
        const val CHARACTERS_KEY = "characters_key"
    }
}