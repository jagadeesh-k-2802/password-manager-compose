package com.jagadeesh.passwordmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.jagadeesh.passwordmanager.core.CryptoManager
import com.jagadeesh.passwordmanager.data.models.UserSettings
import com.jagadeesh.passwordmanager.data.repository.MasterPasswordRepositoryImpl
import com.jagadeesh.passwordmanager.data.serializers.UserSettingsSerializer
import com.jagadeesh.passwordmanager.domain.repository.MasterPasswordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences.json";

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMasterPasswordRepository(dataStore: DataStore<UserSettings>): MasterPasswordRepository {
        return MasterPasswordRepositoryImpl(
            dataStore = dataStore
        )
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<UserSettings> {
        return DataStoreFactory.create(
            serializer = UserSettingsSerializer(cryptoManager = CryptoManager()),
            produceFile = { appContext.dataStoreFile(USER_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
