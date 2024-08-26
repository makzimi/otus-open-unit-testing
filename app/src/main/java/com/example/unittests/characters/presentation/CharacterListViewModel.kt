package com.example.unittests.characters.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unittests.R
import com.example.unittests.characters.data.CharacterEntity
import com.example.unittests.characters.data.CharacterLocalDataSource
import com.example.unittests.characters.data.FavoritesDataSource
import com.example.unittests.characters.domain.AddFavoriteUseCase
import com.example.unittests.characters.domain.Character
import com.example.unittests.characters.domain.FavoriteCharacter
import com.example.unittests.characters.domain.RemoveFavoriteUseCase
import com.example.unittests.common.network.RMService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val rmService: RMService,
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val favoritesDataSource: FavoritesDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersScreenState())
    val state: StateFlow<CharactersScreenState> = _state.asStateFlow()

    fun requestCharacters() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val characters = rmService.getCharacters().results
                characterLocalDataSource.saveCharacters(
                    characters.map {
                        CharacterEntity(
                            id = it.id.toString(),
                            name = it.name,
                            image = it.image,
                        )
                    }
                )
            }
        }

        combine(
            characterLocalDataSource
                .consumeCharacters()
                .map { characterEntities ->
                    characterEntities.map {
                        Character(
                            id = it.id,
                            name = it.name,
                            image = it.image,
                        )
                    }
                }
                .flowOn(Dispatchers.IO),
            favoritesDataSource.consumeFavorites()
                .map { favoriteEntities ->
                    favoriteEntities.map {
                        FavoriteCharacter(
                            id = it.id,
                        )
                    }
                }
                .flowOn(Dispatchers.IO),
        ) { characters, favorites ->
            val favoritesSet = favorites.toSet()
            characters.map { character ->
                CharacterState(
                    id = character.id,
                    name = character.name,
                    image = character.image,
                    isFavorite = favoritesSet.map(FavoriteCharacter::id).contains(character.id)
                )
            }
        }
            .onStart {
                _state.update { screenState -> screenState.copy(isLoading = true) }
            }
            .onEach { listState ->
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        characterListState = listState,
                    )
                }
            }
            .catch {
                _state.update { screenState ->
                    screenState.copy(
                        hasError = true,
                        errorRes = R.string.error_wile_loading_data,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        requestCharacters()
    }

    fun errorHasShown() {
        _state.update { screenState -> screenState.copy(hasError = false) }
    }

    fun addToFavorites(favoriteId: String) {
        viewModelScope.launch {
            addFavoriteUseCase(favoriteCharacter = FavoriteCharacter(favoriteId))
        }
    }

    fun removeFromFavorites(favoriteId: String) {
        viewModelScope.launch {
            removeFavoriteUseCase(favoriteCharacter = FavoriteCharacter(favoriteId))
        }
    }
}
