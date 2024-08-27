package com.example.unittests

import com.example.unittests.characters.data.CharacterDataMapper
import com.example.unittests.characters.data.CharacterDomainMapper
import com.example.unittests.characters.data.CharacterEntity
import com.example.unittests.characters.data.CharacterLocalDataSource
import com.example.unittests.characters.data.CharacterRemoteDataSource
import com.example.unittests.characters.data.CharacterRepositoryImpl
import com.example.unittests.characters.data.FavoriteEntity
import com.example.unittests.characters.data.FavoritesDataSource
import com.example.unittests.characters.data.FavoritesDomainMapper
import com.example.unittests.characters.data.FavoritesRepositoryImpl
import com.example.unittests.characters.domain.AddFavoriteUseCase
import com.example.unittests.characters.domain.ConsumeCharactersUseCase
import com.example.unittests.characters.domain.ConsumeFavoritesUseCase
import com.example.unittests.characters.domain.RemoveFavoriteUseCase
import com.example.unittests.characters.presentation.CharacterListViewModel
import com.example.unittests.characters.presentation.CharacterState
import com.example.unittests.characters.presentation.CharactersScreenState
import com.example.unittests.characters.presentation.StateFactory
import com.example.unittests.characters.presentation.StateFactoryImpl
import com.example.unittests.common.network.dto.CharacterDto
import com.example.unittests.finish.CharacterLocalDataSourceFake
import com.example.unittests.finish.FavoritesDataSourceFake
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class IntegrationTest {

    private lateinit var sut: CharacterListViewModel

    @Mock
    lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val ioDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // TODO Uncomment
//        val favoritesRepository = FavoritesRepositoryImpl(
//            favoritesDataSource = FavoritesDataSourceFake(),
//            domainMapper = FavoritesDomainMapper(),
//            dispatcher = ioDispatcher,
//        )
//        val characterRepository = CharacterRepositoryImpl(
//            characterLocalDataSource = CharacterLocalDataSourceFake(),
//            characterRemoteDataSource = characterRemoteDataSource,
//            dataMapper = CharacterDataMapper(),
//            domainMapper = CharacterDomainMapper(),
//            dispatcher = ioDispatcher,
//        )
//        val consumeCharactersUseCase = ConsumeCharactersUseCase(
//            characterRepository = characterRepository,
//        )
//        val consumeFavoritesUseCase = ConsumeFavoritesUseCase(
//            favoritesRepository = favoritesRepository,
//        )
//        val addFavoriteUseCase = AddFavoriteUseCase(
//            favoritesRepository = favoritesRepository,
//        )
//        val removeFavoriteUseCase = RemoveFavoriteUseCase(
//            favoritesRepository = favoritesRepository,
//        )
//        sut = com.example.unittests.characters.presentation.finish.CharacterListViewModel(
//            consumeCharactersUseCase = consumeCharactersUseCase,
//            stateFactory = StateFactoryImpl(),
//            consumeFavoritesUseCase = consumeFavoritesUseCase,
//            addFavoriteUseCase = addFavoriteUseCase,
//            removeFavoriteUseCase = removeFavoriteUseCase,
//        )
    }

    private fun characterState(
        id: String = "",
        name: String = "",
        image: String = "",
        isFavorite: Boolean = false,
    ): CharacterState {
        return CharacterState(
            id = id,
            name = name,
            image = image,
            isFavorite = isFavorite
        )
    }

    private fun stateWithCharacters(vararg characters: CharacterState): CharactersScreenState {
        return CharactersScreenState(isLoading = false, characterListState = characters.toList())
    }

    private suspend fun charactersFromServer(vararg characters: CharacterDto) {
        whenever(characterRemoteDataSource.getCharacters()).thenReturn(characters.toList())
    }

    private fun create(
        id: String = "",
        name: String = "",
        image: String = "",
    ): CharacterDto {
        return CharacterDto(
            id = id.toLong(),
            name = name,
            image = image,
        )
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        context = UnconfinedTestDispatcher(),
        testBody = testBody,
    )
}
