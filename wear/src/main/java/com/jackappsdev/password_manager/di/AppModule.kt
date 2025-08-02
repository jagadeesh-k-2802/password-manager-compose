package com.jackappsdev.password_manager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.jackappsdev.password_manager.BuildConfig
import com.jackappsdev.password_manager.data.local.DATABASE_NAME
import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.data.local.PasswordDatabase
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.data.repository.PassphraseRepositoryImpl
import com.jackappsdev.password_manager.data.repository.PasswordItemRepositoryImpl
import com.jackappsdev.password_manager.data.repository.UserPreferencesRepositoryImpl
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.services.DataLayerListenerActions
import com.jackappsdev.password_manager.services.DataLayerListenerActionsImpl
import com.jackappsdev.password_manager.shared.core.DataStoreEncryptionSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences.json"

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
    fun providePassphraseRepository(
        dataStore: DataStore<UserSettings>
    ): PassphraseRepository {
        return PassphraseRepositoryImpl(dataStore = dataStore)
    }

    @Singleton
    @Provides
    fun providePasswordDao(
        passwordDatabase: PasswordDatabase
    ): PasswordDao {
        return passwordDatabase.passwordDao()
    }

    @Provides
    @Singleton
    fun providePasswordItemRepository(
        passwordDao: PasswordDao
    ): PasswordItemRepository {
        return PasswordItemRepositoryImpl(
            passwordDao = passwordDao
        )
    }

    @Singleton
    @Provides
    fun providesDataLayerListenerActions(
        @ApplicationContext appContext: Context,
        userPreferencesRepository: UserPreferencesRepository,
        passphraseRepository: PassphraseRepository,
        passwordItemRepository: PasswordItemRepository
    ): DataLayerListenerActions {
        return DataLayerListenerActionsImpl(
            appContext,
            passphraseRepository = passphraseRepository,
            userPreferencesRepository = userPreferencesRepository,
            passwordItemRepository = passwordItemRepository
        )
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<UserSettings> {
        return DataStoreFactory.create(
            serializer = DataStoreEncryptionSerializer(
                context = appContext,
                serializer = UserSettings.serializer(),
                defaultValue = UserSettings()
            ),
            produceFile = { appContext.dataStoreFile(USER_PREFERENCES) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideSupportFactory(): SupportOpenHelperFactory {
        val encryptionKey = BuildConfig.ENCRYPTION_SECRET_KEY.toByteArray(Charsets.UTF_8)
        return SupportOpenHelperFactory(encryptionKey)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context,
        supportFactory: SupportOpenHelperFactory
    ): PasswordDatabase {
        return Room.databaseBuilder(appContext, PasswordDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration(true)
            .build()
    }
}
