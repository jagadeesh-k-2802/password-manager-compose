package com.jackappsdev.password_manager.autofill.model

import android.service.autofill.FillResponse

sealed interface FillResult {
    data class Success(val response: FillResponse) : FillResult
    data object Cancelled : FillResult
}
