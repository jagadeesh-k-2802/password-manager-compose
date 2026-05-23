package com.jackappsdev.password_manager.shared.constants

// General Constants
const val EMPTY_STRING = ""
const val ZERO = 0

// Data Communications Between Mobile <-> Wear OS (Change in [AndroidManifest.xml] for Wear OS)

// Data Keys
const val SET_PIN = "/set-watch-pin"
const val UPSERT_PASSWORD = "/upsert-password"
const val DELETE_PASSWORD = "/delete-password"
const val WIPE_DATA = "/wipe-data"
const val REQUEST_VERSION = "/request-version"
const val VERSION_INFO = "/version-info"
const val REMOVE_PASSWORD_FROM_WATCH = "/remove-password-from-watch"

// Data Keys
const val KEY_PIN = "KEY-PIN"
const val KEY_PASSWORD = "KEY-PASSWORD"
const val KEY_PASSWORD_ID = "KEY-PASSWORD-ID"

// Verify App Installation
const val VERIFY_WEAR_APP = "verify_remote_wear_app"

// URI
const val PLAY_STORE_APP_URI = "https://play.google.com/store/apps/details?id=com.jackappsdev.password_manager"
