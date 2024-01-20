/*
package com.example.ngiu.functions

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

@RequiresApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val context: Context) {

//    private val biometricPrompt: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
//        .setTitle("Biometric login for my app")
//        .setSubtitle("Log in using your biometric credential")
//        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
//        .build()
    private val cancellationSignal: CancellationSignal = CancellationSignal()

    val biometricPrompt = BiometricPrompt.Builder(context)
        .setTitle("Title for your prompt")
        .setSubtitle("Subtitle for your prompt")
        .setDescription("Description for your prompt")
        .setNegativeButton("Cancel", executor, DialogInterface.OnClickListener { dialog, which ->
            // Handle cancel event
        })
        .build()

    @TargetApi(Build.VERSION_CODES.M)
    fun authenticate(callback: BiometricPrompt.AuthenticationCallback) {
        if (checkBiometricSupport()) {
            if (checkPermission()) {
                biometricPrompt.authenticators(
                    cancellationSignal,
                    context.mainExecutor,
                    callback
                )
            }
        }
    }

    private fun checkBiometricSupport(): Boolean {
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate()
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.USE_BIOMETRIC),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 123
    }

}
*/
