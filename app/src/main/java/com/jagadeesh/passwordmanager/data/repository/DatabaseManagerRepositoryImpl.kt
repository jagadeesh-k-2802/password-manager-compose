package com.jagadeesh.passwordmanager.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jagadeesh.passwordmanager.data.local.DATABASE_NAME
import com.jagadeesh.passwordmanager.data.local.PasswordDao
import com.jagadeesh.passwordmanager.domain.repository.DatabaseManagerRepository
import com.jagadeesh.passwordmanager.presentation.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DatabaseManagerRepositoryImpl(
    private val appContext: Context,
    private val passwordDao: PasswordDao
) : DatabaseManagerRepository {
    override suspend fun importData(path: String) {
        withContext(Dispatchers.IO) {
            val input = appContext.contentResolver.openInputStream(Uri.parse(path))
            val output = File(appContext.getDatabasePath(DATABASE_NAME).absolutePath)
            val outputStream = FileOutputStream(output)
            input?.copyTo(outputStream)
            input?.close()
            outputStream.flush()
            outputStream.close()
            File(appContext.getDatabasePath(("$DATABASE_NAME-wal")).absolutePath).delete()
            File(appContext.getDatabasePath(("$DATABASE_NAME-shm")).absolutePath).delete()

            // Restart Application
            val intent = Intent(appContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)
            Runtime.getRuntime().exit(0)
        }
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
