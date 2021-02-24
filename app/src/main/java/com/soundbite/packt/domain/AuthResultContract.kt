package com.soundbite.packt.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

/**
 * Creates a concrete class for handling the Firebase authorization flow.
 */
class AuthResultContract : ActivityResultContract<Int, IdpResponse>() {
    companion object {
        const val REQUEST_CODE = 2021
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    /**
     * Creates an intent to start the AuthUI workflow.
     *
     * @param context The context for the Intent.
     * @param input The input that will create the Intent.
     *
     * @return An Intent that will start the Firebase authorization flow.
     */
    override fun createIntent(context: Context, input: Int?): Intent =
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
            .apply {
                putExtra("REQUEST_CODE", input)
            }

    /**
     * Converts the result obtained from [Activity.onActivityResult] to a nullable [IdpResponse].
     *
     * @param resultCode The result code obtained from the result.
     * @param intent The intent that [Activity.onActivityResult] was called with.
     *
     * @return A nullable IdpResponse parsed from the calling Intent.
     */
    override fun parseResult(resultCode: Int, intent: Intent?): IdpResponse? =
        when (resultCode) {
            Activity.RESULT_OK -> IdpResponse.fromResultIntent(intent)
            else -> null
        }
}
