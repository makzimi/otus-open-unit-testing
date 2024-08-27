package com.example.unittests.finish

import com.example.unittests.MainDispatcherRule
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
import com.example.unittests.characters.presentation.finish.CharacterListViewModel
import com.example.unittests.characters.presentation.CharacterState
import com.example.unittests.characters.presentation.CharactersScreenState
import com.example.unittests.characters.presentation.StateFactory
import com.example.unittests.characters.presentation.StateFactoryImpl
import com.example.unittests.common.network.dto.CharacterDto
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

class CharacterLocalDataSourceFake : CharacterLocalDataSource {
    private val state = MutableStateFlow<List<CharacterEntity>>(listOf())
    override fun consumeCharacters(): Flow<List<CharacterEntity>> = state.asStateFlow()
    override suspend fun saveCharacters(characters: List<CharacterEntity>) {
        state.value = characters
    }
}

class FavoritesDataSourceFake : FavoritesDataSource {
    private val state = MutableStateFlow<List<FavoriteEntity>>(listOf())

    override fun consumeFavorites(): Flow<List<FavoriteEntity>> = state.asStateFlow()

    override suspend fun saveFavorite(favoriteEntity: FavoriteEntity) {
        state.update { current ->
            current.toMutableList().apply { add(favoriteEntity) }
        }
    }

    override suspend fun removeFavorite(favoriteEntity: FavoriteEntity) {
        state.update { current ->
            current.toMutableList().apply { removeIf { it.id == favoriteEntity.id} }
        }
    }
}

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
        val favoritesRepository = FavoritesRepositoryImpl(
            favoritesDataSource = FavoritesDataSourceFake(),
            domainMapper = FavoritesDomainMapper(),
            dispatcher = ioDispatcher,
        )
        val characterRepository = CharacterRepositoryImpl(
            characterLocalDataSource = CharacterLocalDataSourceFake(),
            characterRemoteDataSource = characterRemoteDataSource,
            dataMapper = CharacterDataMapper(),
            domainMapper = CharacterDomainMapper(),
            dispatcher = ioDispatcher,
        )
        val consumeCharactersUseCase = ConsumeCharactersUseCase(
            characterRepository = characterRepository,
        )
        val consumeFavoritesUseCase = ConsumeFavoritesUseCase(
            favoritesRepository = favoritesRepository,
        )
        val addFavoriteUseCase = AddFavoriteUseCase(
            favoritesRepository = favoritesRepository,
        )
        val removeFavoriteUseCase = RemoveFavoriteUseCase(
            favoritesRepository = favoritesRepository,
        )
        sut = CharacterListViewModel(
            consumeCharactersUseCase = consumeCharactersUseCase,
            stateFactory = StateFactoryImpl(),
            consumeFavoritesUseCase = consumeFavoritesUseCase,
            addFavoriteUseCase = addFavoriteUseCase,
            removeFavoriteUseCase = removeFavoriteUseCase,
        )
    }

    @Test
    fun `requestCharacters EXPECT show all three states`() = runTestUnconfined {
        // arrange
        charactersFromServer(create(id = "1"), create(id = "2"))
        val expectedInitialState = CharactersScreenState()
        val expectedLoadingState = CharactersScreenState(isLoading = true)
        val expectedDataState = CharactersScreenState(
            isLoading = false,
            characterListState = listOf(
                characterState(id = "1"),
                characterState(id = "2"),
            )
        )
        val (job, results) = collectResults()

        // act
        sut.requestCharacters()
        ioDispatcher.scheduler.runCurrent()

        // assert
        assertEquals(3, results.size)
        assertEquals(expectedInitialState, results[0])
        assertEquals(expectedLoadingState, results[1])
        assertEquals(expectedDataState, results[2])
        job.cancel()
    }

    @Test
    fun `addToFavorites WHEN add favorite EXPECT favorite flag is true for character`() = runTestUnconfined {
        // arrange
        charactersFromServer(create(id = "1"), create(id = "2"))
        val expectedDataState = stateWithCharacters(
            characterState(id = "1", isFavorite = true),
            characterState(id = "2", isFavorite = false),
        )
        val (job, results) = collectResults()
        sut.requestCharacters()
        ioDispatcher.scheduler.runCurrent()

        // act
        sut.addToFavorites(favoriteId = "1")
        ioDispatcher.scheduler.runCurrent()

        // assert
        assertEquals(4, results.size)
        assertEquals(expectedDataState, results[3])
        job.cancel()
    }

    @Test
    fun `removeFromFavorites WHEN remove favorite EXPECT favorite flag is false for character`() = runTestUnconfined {
        // arrange
        charactersFromServer(create(id = "1"), create(id = "2"))
        val expectedDataState = stateWithCharacters(
            characterState(id = "1", isFavorite = false),
            characterState(id = "2", isFavorite = true),
        )
        val (job, results) = collectResults()
        sut.requestCharacters()
        ioDispatcher.scheduler.runCurrent()
        sut.addToFavorites(favoriteId = "1")
        ioDispatcher.scheduler.runCurrent()
        sut.addToFavorites(favoriteId = "2")
        ioDispatcher.scheduler.runCurrent()

        // act
        sut.removeFromFavorites(favoriteId = "1")
        ioDispatcher.scheduler.runCurrent()

        // assert
        assertEquals(6, results.size)
        assertEquals(expectedDataState, results[5])
        job.cancel()
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

    private fun CoroutineScope.collectResults(): Pair<Job, List<CharactersScreenState>> {
        val results = mutableListOf<CharactersScreenState>()
        val job = sut.state
            .onEach(results::add)
            .launchIn(this)

        return (job to results)
    }
}
