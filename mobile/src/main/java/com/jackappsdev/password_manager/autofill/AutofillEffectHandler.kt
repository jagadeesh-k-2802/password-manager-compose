package com.jackappsdev.password_manager.autofill

import android.content.Context
import android.view.autofill.AutofillId
import com.jackappsdev.password_manager.autofill.AutofillService.Companion.buildFillResponseForSelection
import com.jackappsdev.password_manager.autofill.model.FillResult
import com.jackappsdev.password_manager.domain.model.PasswordItemModel

class AutofillEffectHandler(
    private val context: Context,
    private val usernameId: AutofillId?,
    private val passwordId: AutofillId?,
    private val onResult: (FillResult) -> Unit
) {

    fun onItemSelected(item: PasswordItemModel) {
        val item = item
        val response = buildFillResponseForSelection(
            packageName = context.packageName,
            label = item.name,
            usernameId = usernameId,
            passwordId = passwordId,
            username = item.username,
            password = item.password
        )
        onResult(
            if (response != null) {
                FillResult.Success(response)
            } else {
                FillResult.Cancelled
            }
        )
    }
}
