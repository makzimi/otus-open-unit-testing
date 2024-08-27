package com.example.unittests.di

import com.example.unittests.characters.presentation.StateFactory
import com.example.unittests.characters.presentation.StateFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface CharactersModule {
    @Binds
    fun bindStateFactory(
        stateFactoryImpl: StateFactoryImpl
    ): StateFactory
}
