package com.opentech.automatid.demoapp.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository = SettingsRepository()

    val idMethodSettingsLiveData: LiveData<IdMethodSettings> = MutableLiveData(settingsRepository.readIdMethods(getApplication()))
    val retryOnErrorsLiveData: LiveData<Boolean> = MutableLiveData(settingsRepository.readRetryOnErrors(getApplication()))

    var tcAccepted: Boolean
        get() = settingsRepository.readTcAccepted(getApplication())
        set(value) = settingsRepository.writeTcAccepted(getApplication(), value)

    fun isValidIdMethodSettings(settings: IdMethodSettings) = settings.isValid()

    fun maybeSetIdMethodSettings(settings: IdMethodSettings): Boolean {
        return if (settings.isValid()) {
            settingsRepository.writeIdMethods(getApplication(), settings)
            (idMethodSettingsLiveData as MutableLiveData<IdMethodSettings>).value = settings
            true
        } else {
            false
        }
    }

    fun setRetryOnErrors(retryOnErrors: Boolean) {
        settingsRepository.writeRetryOnErrors(getApplication(), retryOnErrors)
        (retryOnErrorsLiveData as MutableLiveData<Boolean>).value = retryOnErrors
    }
}
