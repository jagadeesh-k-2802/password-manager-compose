package com.jackappsdev.password_manager.shared.constants

// General Constants
const val EMPTY_STRING = ""
const val ZERO = 0

// Data Communications Between Mobile <-> Wear OS (Change in [AndroidManifest.xml] for Wear OS)

// Data Keys
const val KEY_PIN = "KEY-PIN"
const val KEY_PASSWORD = "KEY-PASSWORD"

// Actions
const val SET_PIN = "/set-watch-pin"
const val UPSERT_PASSWORD = "/upsert-password"
const val DELETE_PASSWORD = "/delete-password"
const val WIPE_DATA = "/wipe-data"

// Verify App Installation
const val VERIFY_WEAR_APP = "verify_remote_wear_app"

// URI
const val PLAY_STORE_APP_URI = "https://play.google.com/store/apps/details?id=com.jackappsdev.password_manager"
