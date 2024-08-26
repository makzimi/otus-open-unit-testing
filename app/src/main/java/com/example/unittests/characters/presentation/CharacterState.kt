package com.example.unittests.characters.presentation

data class CharactersScreenState(
    val isLoading: Boolean = false,
    val characterListState: List<CharacterState> = emptyList(),
    val hasError: Boolean = false,
    val errorRes: Int = 0,
)

data class CharacterState(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val isFavorite: Boolean = false,
)
