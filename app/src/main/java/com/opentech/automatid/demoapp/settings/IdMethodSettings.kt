package com.opentech.automatid.demoapp.settings

data class IdMethodSettings(
    val passportEnabled: Boolean,
    val idDocumentEnabled: Boolean,
    val paymentCardSetting: PaymentCardSetting,
) {
    fun isValid(): Boolean {
        return idDocumentEnabled || passportEnabled || paymentCardSetting != PaymentCardSetting.Off
    }
}

enum class PaymentCardSetting {
    Off,
    ValidCard,
    InvalidCard
}
