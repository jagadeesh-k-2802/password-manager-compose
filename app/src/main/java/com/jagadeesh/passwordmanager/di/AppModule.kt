package com.jagadeesh.passwordmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.jagadeesh.passwordmanager.core.CryptoManager
import com.jagadeesh.passwordmanager.data.local.DATABASE_NAME
import com.jagadeesh.passwordmanager.data.local.PasswordDatabase
import com.jagadeesh.passwordmanager.data.models.UserSettings
import com.jagadeesh.passwordmanager.data.repository.PasswordItemRepositoryImpl
import com.jagadeesh.passwordmanager.data.repository.UserPreferencesRepositoryImpl
import com.jagadeesh.passwordmanager.data.serializers.UserSettingsSerializer
import com.jagadeesh.passwordmanager.domain.repository.PasswordItemRepository
import com.jagadeesh.passwordmanager.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences.json";

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<UserSettings>
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            dataStore = dataStore
        )
    }

    @Provides
    @Singleton
    fun providePasswordItemRepository(
        passwordDatabase: PasswordDatabase
    ): PasswordItemRepository {
        return PasswordItemRepositoryImpl(
            passwordDatabase = passwordDatabase
        )
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<UserSettings> {
        return DataStoreFactory.create(
            serializer = UserSettingsSerializer(cryptoManager = CryptoManager()),
            produceFile = { appContext.dataStoreFile(USER_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context,
        userPreferencesRepository: UserPreferencesRepository
    ): PasswordDatabase {
        val userPassphrase = userPreferencesRepository.getPassword()?.toCharArray()
        val passphrase = SQLiteDatabase.getBytes(userPassphrase)
        val supportFactory = SupportFactory(passphrase)

        return Room.databaseBuilder(appContext, PasswordDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration()
            .build()
    }
}
