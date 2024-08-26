package com.example.unittests.characters.presentation.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unittests.R
import com.example.unittests.characters.domain.AddFavoriteUseCase
import com.example.unittests.characters.domain.ConsumeCharactersUseCase
import com.example.unittests.characters.domain.ConsumeFavoritesUseCase
import com.example.unittests.characters.domain.FavoriteCharacter
import com.example.unittests.characters.domain.RemoveFavoriteUseCase
import com.example.unittests.characters.presentation.CharactersScreenState
import com.example.unittests.characters.presentation.StateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val consumeCharactersUseCase: ConsumeCharactersUseCase,
    private val stateFactory: StateFactory,
    private val consumeFavoritesUseCase: ConsumeFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersScreenState())
    val state: StateFlow<CharactersScreenState> = _state.asStateFlow()

    fun requestCharacters() {
        combine(
            consumeCharactersUseCase(),
            consumeFavoritesUseCase(),
        ) { characters, favorites ->
            val favoritesSet = favorites.toSet()
            characters.map { character -> stateFactory.create(character, favoritesSet) }
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
