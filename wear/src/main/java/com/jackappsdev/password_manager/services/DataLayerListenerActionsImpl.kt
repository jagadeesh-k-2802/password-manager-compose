package com.jackappsdev.password_manager.services

import android.content.Context
import android.content.Intent
import com.jackappsdev.password_manager.data.local.DATABASE_NAME
import com.jackappsdev.password_manager.domain.mappers.toPasswordItemModel
import com.jackappsdev.password_manager.domain.model.PasswordItemModel
import com.jackappsdev.password_manager.domain.repository.PassphraseRepository
import com.jackappsdev.password_manager.domain.repository.PasswordItemRepository
import com.jackappsdev.password_manager.domain.repository.UserPreferencesRepository
import com.jackappsdev.password_manager.presentation.MainActivity
import com.jackappsdev.password_manager.shared.data.dto.PasswordItemDto
import kotlinx.serialization.json.Json

/**
 * Implementation of [DataLayerListenerActions] for [DataLayerListenerService] actions.
 */
class DataLayerListenerActionsImpl(
    private val context: Context,
    private val passphraseRepository: PassphraseRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val passwordItemRepository: PasswordItemRepository
) : DataLayerListenerActions {
    override suspend fun setPin(newPin: String?) {
        // Split the time part it is added to the same
        // It is appended so user can change the PIN again instantly
        if (newPin.isNullOrBlank()) return
        val pin = newPin.split(" ").first()

        if (userPreferencesRepository.hasPinSet()) {
            passphraseRepository.updatePin(pin)
        } else {
            userPreferencesRepository.setPin(pin)
        }
    }

    override suspend fun upsertPasswordItem(serializedPasswordItem: String?) {
        if (!userPreferencesRepository.hasPinSet()) return
        val item = jsonToPasswordItemModel(serializedPasswordItem) ?: return
        passwordItemRepository.upsertPasswordItem(item)
    }

    override suspend fun deletePasswordItem(serializedPasswordItem: String?) {
        if (!userPreferencesRepository.hasPinSet()) return
        val item = jsonToPasswordItemModel(serializedPasswordItem) ?: return
        passwordItemRepository.deletePasswordItem(item)
    }

    override suspend fun onWipeDataPath() {
        if (!userPreferencesRepository.hasPinSet()) return
        userPreferencesRepository.setPin(null)
        passwordItemRepository.deleteAllPasswords()
        context.deleteDatabase(DATABASE_NAME)
        context.sendBroadcast(Intent(MainActivity.PIN_CHANGE_ACTION))
    }

    private fun jsonToPasswordItemModel(serializedString: String?): PasswordItemModel? {
        if (serializedString.isNullOrBlank()) return null
        return Json.decodeFromString<PasswordItemDto>(serializedString).toPasswordItemModel()
    }
}
