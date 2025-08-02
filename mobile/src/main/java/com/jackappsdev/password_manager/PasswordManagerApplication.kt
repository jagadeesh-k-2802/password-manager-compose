package com.jackappsdev.password_manager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PasswordManagerApplication : Application() {

    companion object {
        const val SQL_CIPHER_NATIVE_LIB = "sqlcipher"
    }

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary(SQL_CIPHER_NATIVE_LIB)
    }
}
