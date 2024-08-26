package com.example.unittests.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.unittests.characters.data.CharacterLocalDataSource
import com.example.unittests.characters.data.CharacterLocalDataSourceImpl
import com.example.unittests.characters.data.CharacterRemoteDataSource
import com.example.unittests.characters.data.CharacterRemoteDataSourceImpl
import com.example.unittests.characters.data.CharacterRepositoryImpl
import com.example.unittests.characters.data.FavoritesDataSource
import com.example.unittests.characters.data.FavoritesDataSourceImpl
import com.example.unittests.characters.data.FavoritesRepositoryImpl
import com.example.unittests.characters.domain.CharacterRepository
import com.example.unittests.characters.domain.FavoritesRepository
import com.example.unittests.common.network.RMService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app")

@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @Singleton
    @Binds
    fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository

    @Singleton
    @Binds
    fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository

    companion object {
        @Singleton
        @Provides
        fun provideFavoritesDataSource(
            dataStore: DataStore<Preferences>,
        ): FavoritesDataSource {
            return FavoritesDataSourceImpl(
                dataStore = dataStore
            )
        }

        @Singleton
        @Provides
        fun provideRMRetrofitService(
            retrofit: Retrofit
        ): RMService {
            return retrofit.create(RMService::class.java)
        }

        @Singleton
        @Provides
        fun provideCoroutineDispatcher(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        @Singleton
        @Provides
        fun provideDataStoreOfPreferences(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.appDataStore
        }

        @Singleton
        @Provides
        fun provideCharacterLocalDataSource(
            dataStore: DataStore<Preferences>
        ): CharacterLocalDataSource {
            return CharacterLocalDataSourceImpl(
                dataStore = dataStore
            )
        }

        @Singleton
        @Provides
        fun provideCharacterRemoteDataSource(
            rmService: RMService
        ): CharacterRemoteDataSource {
            return CharacterRemoteDataSourceImpl(
                rmService = rmService
            )
        }
    }
}