package com.jackappsdev.password_manager.data.repository

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import com.jackappsdev.password_manager.shared.core.EncryptedSQLiteOpenHelper
import com.jackappsdev.password_manager.data.local.DATABASE_NAME
import com.jackappsdev.password_manager.data.local.DATABASE_VERSION
import com.jackappsdev.password_manager.data.local.dao.PasswordDao
import com.jackappsdev.password_manager.domain.repository.DatabaseBackupManager
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.presentation.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import androidx.core.net.toUri
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.jackappsdev.password_manager.core.parseModifiedTime

class DatabaseBackupManagerImpl(
    private val appContext: Context,
    private val passwordDao: PasswordDao,
    private val passphraseRepository: PassphraseRepository
) : DatabaseBackupManager {

    override suspend fun importDatabase(path: String, password: String): Boolean {
        val tempDatabaseName = "temp.db"

        withContext(Dispatchers.IO) {
            val tempDB = File(appContext.getDatabasePath(tempDatabaseName).absolutePath)
            tempDB.createNewFile()
            val input = appContext.contentResolver.openInputStream(path.toUri())
            val outputStream = FileOutputStream(tempDB)
            input?.copyTo(outputStream)
            input?.close()
            outputStream.flush()
            outputStream.close()
        }

        try {
            val db = EncryptedSQLiteOpenHelper(
                appContext,
                tempDatabaseName,
                null,
                DATABASE_VERSION
            )
            db.getReadableDatabase(password.toCharArray())
        } catch (e: SQLiteException) {
            println(e)
            return false
        }

        val tempDB = File(appContext.getDatabasePath(tempDatabaseName).absolutePath)
        tempDB.delete()

        withContext(Dispatchers.IO) {
            // Copy to current database if password is valid
            val output = File(appContext.getDatabasePath(DATABASE_NAME).absolutePath)
            val input = appContext.contentResolver.openInputStream(path.toUri())
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

        // Application will restart before it returns so we only care for 'false' value
        return true
    }

    override suspend fun exportDatabase(path: String) {
        withContext(Dispatchers.IO) {
            passwordDao.checkpoint() // To save changes without it DB will be empty
            val from = File(appContext.getDatabasePath(DATABASE_NAME).absolutePath)
            val input = FileInputStream(from)
            val output = appContext.contentResolver.openOutputStream(path.toUri())
            if (output != null) input.copyTo(output)
            output?.flush()
            output?.close()
            input.close()
        }
    }

    override suspend fun exportCsv(path: String) {
        withContext(Dispatchers.IO) {
            passwordDao.checkpoint() // To save changes without it DB will be empty
            val passwordsWithCategories = passwordDao.getAllPasswordWithCategoryEntities()
            val output = appContext.contentResolver.openOutputStream(path.toUri())

            output?.let {
                csvWriter().open(output) {
                    writeRow(
                        "Name",
                        "Username",
                        "Password",
                        "Website",
                        "Notes",
                        "Category",
                        "Last Updated At"
                    )

                    passwordsWithCategories.forEach { item ->
                        writeRow(
                            item.passwordItem.name,
                            item.passwordItem.username,
                            item.passwordItem.password,
                            item.passwordItem.website,
                            item.passwordItem.notes,
                            item.categoryEntity?.name ?: "No Category",
                            parseModifiedTime(appContext, item.passwordItem.createdAt)
                        )
                    }
                }
            }
        }
    }
}
