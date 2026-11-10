package com.jakger.ghcrange_mobile.ui.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

internal const val EXTRA_PERMISSION_EXPLANATION = "explain_health_permissions"

internal class PermissionsRationaleContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, PermissionsRationaleActivity::class.java)
            .putExtra(EXTRA_PERMISSION_EXPLANATION, true)

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        resultCode == Activity.RESULT_OK
}
