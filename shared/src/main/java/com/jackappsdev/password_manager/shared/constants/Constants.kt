package com.jackappsdev.password_manager.shared.constants

// Data Communications Between Mobile & Wear OS (Change in [AndroidManifest.xml] for Wear OS)

const val KEY_PIN = "KEY-PIN"
const val KEY_PASSWORD = "KEY-PASSWORD"

const val SET_PIN = "/set-watch-pin"
const val UPSERT_PASSWORD = "/upsert-password"
const val DELETE_PASSWORD = "/delete-password"
const val WIPE_DATA = "/wipe-data"

const val VERIFY_WEAR_APP = "verify_remote_wear_app"

// URI
const val PLAY_STORE_APP_URI = "https://play.google.com/store/apps/details?id=com.jackappsdev.password_manager"

// Colors List for Categories
val colorList = listOf(
    "#FF6F61", // Reddish
    "#FFD166", // Orange
    "#06D6A0", // Greenish Cyan
    "#118AB2", // Deep Blue
    "#A5A58D", // Grayish
    "#FF7F11", // Vivid Orange
    "#8338EC", // Purple
    "#80D4E9", // Sky Blue
    "#8E9AFC", // Periwinkle
    "#FFCC5C", // Warm Yellow
    "#D72638", // Crimson Red
    "#3F88C5", // Royal Blue
    "#2E294E", // Dark Indigo
    "#1B998B", // Teal
    "#E71D36", // Bright Red
    "#FF9F1C", // Deep Yellow
    "#A7C957", // Olive Green
    "#F4A261", // Soft Orange
    "#264653", // Deep Teal
    "#B56576", // Dusty Rose
    "#6A0572", // Dark Purple
    "#0FA3B1", // Ocean Blue
    "#E63946", // Coral Red
    "#457B9D", // Muted Blue
    "#2A9D8F", // Aquatic Green
    "#F77F00", // Burnt Orange
    "#9D4EDD"  // Vivid Violet
)
