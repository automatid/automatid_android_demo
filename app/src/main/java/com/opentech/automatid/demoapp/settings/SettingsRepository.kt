package com.opentech.automatid.demoapp.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsRepository {
    fun readIdMethods(context: Context): IdMethodSettings {
        val preferences = getPreferences(context)

        val passportEnabled = preferences.getBoolean("passportEnabled", true)
        val idDocumentEnabled = preferences.getBoolean("idDocumentEnabled", true)
        val paymentCardSetting = try {
            val s = preferences.getString("paymentCardSetting", null).orEmpty()
            PaymentCardSetting.valueOf(s)
        } catch (e: IllegalArgumentException) {
            PaymentCardSetting.ValidCard
        }

        return IdMethodSettings(passportEnabled, idDocumentEnabled, paymentCardSetting)
    }

    fun writeIdMethods(context: Context, value: IdMethodSettings) {
        getPreferences(context).edit(commit = true) {
            putBoolean("passportEnabled", value.passportEnabled)
            putBoolean("idDocumentEnabled", value.idDocumentEnabled)
            putString("paymentCardSetting", value.paymentCardSetting.name)
        }
    }

    fun readRetryOnErrors(context: Context): Boolean {
        val preferences = getPreferences(context)
        return preferences.getBoolean("retryOnErrors", true)
    }

    fun writeRetryOnErrors(context: Context, value: Boolean) {
        getPreferences(context).edit(commit = true) {
            putBoolean("retryOnErrors", value)
        }
    }

    fun readTcAccepted(context: Context): Boolean {
        val preferences = getPreferences(context)
        return preferences.getBoolean("tcAccepted", false)
    }

    fun writeTcAccepted(context: Context, value: Boolean) {
        getPreferences(context).edit(commit = true) {
            putBoolean("tcAccepted", value)
        }
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("automatid_settings", Context.MODE_PRIVATE)
    }
}
