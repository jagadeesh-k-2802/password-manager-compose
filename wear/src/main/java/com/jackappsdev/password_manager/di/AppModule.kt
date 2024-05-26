package com.jackappsdev.password_manager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.jackappsdev.password_manager.data.local.DATABASE_NAME
import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.data.local.PasswordDatabase
import com.jackappsdev.password_manager.data.models.UserSettings
import com.jackappsdev.password_manager.data.repository.PassphraseRepositoryImpl
import com.jackappsdev.password_manager.data.repository.PasswordItemRepositoryImpl
import com.jackappsdev.password_manager.data.repository.UserPreferencesRepositoryImpl
import com.jackappsdev.password_manager.data.serializers.UserSettingsSerializer
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.shared.core.CryptoManager
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
        dataStore: DataStore<UserSettings>,
        passwordDao: PasswordDao
    ): PassphraseRepository {
        return PassphraseRepositoryImpl(
            dataStore = dataStore,
            passwordDao = passwordDao
        )
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
    fun provideSupportFactory(
        userPreferencesRepository: UserPreferencesRepository
    ): SupportFactory {
        val userPassphrase = userPreferencesRepository.getPin()?.toCharArray()
        val passphrase = SQLiteDatabase.getBytes(userPassphrase)
        return SupportFactory(passphrase)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context,
        supportFactory: SupportFactory
    ): PasswordDatabase {
        return Room.databaseBuilder(appContext, PasswordDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration()
            .build()
    }
}
