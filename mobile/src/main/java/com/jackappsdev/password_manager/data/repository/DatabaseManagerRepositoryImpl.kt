package com.jackappsdev.password_manager.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jackappsdev.password_manager.core.EncryptedSQLiteOpenHelper
import com.jackappsdev.password_manager.data.local.DATABASE_NAME
import com.jackappsdev.password_manager.data.local.PasswordDao
import com.jackappsdev.password_manager.domain.repository.DatabaseManagerRepository
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.presentation.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sqlcipher.database.SQLiteException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DatabaseManagerRepositoryImpl(
    private val appContext: Context,
    private val passwordDao: PasswordDao,
    private val passphraseRepository: PassphraseRepository
) : DatabaseManagerRepository {
    override suspend fun importData(path: String, password: String): Boolean {
        val tempDatabaseName = "temp.db"

        withContext(Dispatchers.IO) {
            val tempDB = File(appContext.getDatabasePath(tempDatabaseName).absolutePath)
            tempDB.createNewFile()
            val input = appContext.contentResolver.openInputStream(Uri.parse(path))
            val outputStream = FileOutputStream(tempDB)
            input?.copyTo(outputStream)
            input?.close()
            outputStream.flush()
            outputStream.close()
        }

        try {
            val db = EncryptedSQLiteOpenHelper(appContext, tempDatabaseName, null, 1)
            db.getReadableDatabase(password.toCharArray())
        } catch (_: SQLiteException) {
            return false
        }

        val tempDB = File(appContext.getDatabasePath(tempDatabaseName).absolutePath)
        tempDB.delete()

        withContext(Dispatchers.IO) {
            // Copy to current database if password is valid
            val output = File(appContext.getDatabasePath(DATABASE_NAME).absolutePath)
            val input = appContext.contentResolver.openInputStream(Uri.parse(path))
            val outputStream = FileOutputStream(output)
            input?.copyTo(outputStream)
            input?.close()
            outputStream.flush()
            outputStream.close()

            // Delete wal, shm so the changes are taken effect
            File(appContext.getDatabasePath(("$DATABASE_NAME-wal")).absolutePath).delete()
            File(appContext.getDatabasePath(("$DATABASE_NAME-shm")).absolutePath).delete()

            // Update password
            passphraseRepository.updatePassword(password)

            // Restart Application
            val intent = Intent(appContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)
            Runtime.getRuntime().exit(0)
        }

        return true // Application will restart before it returns so we only care for 'false' value
    }

    override suspend fun exportData(path: String) {
        withContext(Dispatchers.IO) {
            passwordDao.checkpoint() // To save changes without it DB will be empty
            val from = File(appContext.getDatabasePath(DATABASE_NAME).absolutePath)
            val input = FileInputStream(from)
            val output = appContext.contentResolver.openOutputStream(Uri.parse(path))
            if (output != null) input.copyTo(output)
            output?.flush()
            output?.close()
            input.close()
        }
    }
}
