package com.example.unittests

import com.example.unittests.characters.domain.AddFavoriteUseCase
import com.example.unittests.characters.domain.Character
import com.example.unittests.characters.domain.ConsumeCharactersUseCase
import com.example.unittests.characters.domain.ConsumeFavoritesUseCase
import com.example.unittests.characters.domain.FavoriteCharacter
import com.example.unittests.characters.domain.RemoveFavoriteUseCase
import com.example.unittests.characters.presentation.CharacterListViewModel
import com.example.unittests.characters.presentation.CharacterState
import com.example.unittests.characters.presentation.StateFactory
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    lateinit var sut: CharacterListViewModel

//    @Mock
//    lateinit var consumeCharactersUseCase: ConsumeCharactersUseCase
//
//    @Mock
//    lateinit var consumeFavoritesUseCase: ConsumeFavoritesUseCase
//
//    @Mock
//    lateinit var addFavoriteUseCase: AddFavoriteUseCase
//
//    @Mock
//    lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase
//
//    @Mock
//    lateinit var stateFactory: StateFactory

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
//        whenever(consumeFavoritesUseCase.invoke()).thenReturn(flowOf(listOf()))
//        sut = CharacterListViewModel(
//            consumeCharactersUseCase = consumeCharactersUseCase,
//            stateFactory = stateFactory,
//            consumeFavoritesUseCase = consumeFavoritesUseCase,
//            addFavoriteUseCase = addFavoriteUseCase,
//            removeFavoriteUseCase = removeFavoriteUseCase,
//        )
    }

//    private fun load2Characters(): List<CharacterState> {
//        whenever(consumeCharactersUseCase.invoke())
//            .thenReturn(flowOf(listOf(character(id = "1"), character(id = "2"))))
//
//        val state1 = CharacterState(id = "1")
//        val state2 = CharacterState(id = "2")
//
//        stateFactory.map["1"] = state1
//        stateFactory.map["2"] = state2
//
//        return listOf(state1, state2)
//    }

    private fun character(
        id: String = "",
        name: String = "",
        image: String = "",
    ): Character {
        return Character(
            id = id,
            name = name,
            image = image,
        )
    }
}
