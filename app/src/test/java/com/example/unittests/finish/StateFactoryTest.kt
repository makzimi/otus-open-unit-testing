package com.example.unittests.finish

import com.example.unittests.characters.domain.Character
import com.example.unittests.characters.domain.FavoriteCharacter
import com.example.unittests.characters.presentation.StateFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StateFactoryTest {
    private lateinit var sut: StateFactory

    @Before
    fun setup() {
        sut = StateFactory()
    }

    @Test
    fun `create WHEN favorites are empty EXPECT isFavorite is false`() {
        // arrange
        val character = Character(id = "1", name ="Rick", image = "image")

        // act
        val state = sut.create(character, setOf())

        // assert
        Assert.assertEquals(false, state.isFavorite)
    }

    @Test
    fun `create WHEN my character is in favorites EXPECT isFavorite is true`() {
        // arrange
        val character = Character(id = "1", name ="Rick", image = "image")
        val favorites = setOf(FavoriteCharacter("1"))

        // act
        val productState = sut.create(character, favorites)

        // assert
        Assert.assertEquals(true, productState.isFavorite)
    }

    @Test
    fun `create WHEN my character is not in favorites EXPECT isFavorite is false`() {
        // arrange
        val character = Character(id = "1", name ="Rick", image = "image")
        val favorites = setOf(FavoriteCharacter("2"))

        // act
        val productState = sut.create(character, favorites)

        // assert
        Assert.assertEquals(false, productState.isFavorite)
    }
}
