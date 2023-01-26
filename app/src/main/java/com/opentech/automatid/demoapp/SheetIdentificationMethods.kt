package com.opentech.automatid.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.opentech.automatid.demoapp.databinding.SheetIdentificationMethodsBinding
import com.opentech.automatid.demoapp.settings.IdMethodSettings
import com.opentech.automatid.demoapp.settings.PaymentCardSetting
import com.opentech.automatid.demoapp.settings.SettingsViewModel
import com.opentech.automatid.demoapp.util.Sheet

class SheetIdentificationMethods : Sheet() {
    private val settings: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = SheetIdentificationMethodsBinding.inflate(inflater, container, false)

        settings.idMethodSettingsLiveData.observe(viewLifecycleOwner) { idMethodSettings ->
            binding.chkPassport.isChecked = idMethodSettings.passportEnabled

            binding.chkIdentityCard.isChecked = idMethodSettings.idDocumentEnabled

            when (idMethodSettings.paymentCardSetting) {
                PaymentCardSetting.Off -> {
                    binding.chkPaymentCard.isChecked = false
                    binding.radioGroupCardScan.isGone = true
                }
                PaymentCardSetting.ValidCard -> {
                    binding.chkPaymentCard.isChecked = true
                    binding.radioGroupCardScan.isGone = false
                    binding.radioGroupCardScan.check(R.id.rbCardScanJwtValid)
                }
                PaymentCardSetting.InvalidCard -> {
                    binding.chkPaymentCard.isChecked = true
                    binding.radioGroupCardScan.isGone = false
                    binding.radioGroupCardScan.check(R.id.rbCardScanJwtPanNoMatch)
                }
            }

            binding.lblNoValidSelection.isInvisible = settings.isValidIdMethodSettings(idMethodSettings)
        }

        binding.chkPassport.setOnCheckedChangeListener { _, _ ->
            evaluateIdMethodSettings(binding)
        }

        binding.chkIdentityCard.setOnCheckedChangeListener { _, _ ->
            evaluateIdMethodSettings(binding)
        }

        binding.chkPaymentCard.setOnCheckedChangeListener { _, _ ->
            evaluateIdMethodSettings(binding)
        }

        binding.btnCardScanHelp.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.AppDialog)
                .setTitle(R.string.card_scan_help_title)
                .setMessage(R.string.card_scan_help_body)
                .setPositiveButton(R.string.card_scan_help_button_ok, null)
                .show()
        }

        binding.radioGroupCardScan.isGone = !binding.chkPaymentCard.isChecked

        binding.btnConfirm.setOnClickListener {
            val idMethodSettings = createIdMethodSettings(binding)
            if (settings.maybeSetIdMethodSettings(idMethodSettings)) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun evaluateIdMethodSettings(binding: SheetIdentificationMethodsBinding) {
        val idMethodSettings = createIdMethodSettings(binding)
        val validSettings = settings.isValidIdMethodSettings(idMethodSettings)
        setCloseable(validSettings)
        binding.lblNoValidSelection.isInvisible = validSettings
        binding.btnConfirm.isEnabled = validSettings
    }

    private fun createIdMethodSettings(binding: SheetIdentificationMethodsBinding): IdMethodSettings {
        val passportEnabled = binding.chkPassport.isChecked
        val idDocumentEnabled = binding.chkIdentityCard.isChecked
        val paymentCardChecked = binding.chkPaymentCard.isChecked

        binding.radioGroupCardScan.isGone = !paymentCardChecked

        val paymentCardSetting = when {
            paymentCardChecked && binding.rbCardScanJwtValid.isChecked -> PaymentCardSetting.ValidCard
            paymentCardChecked && binding.rbCardScanJwtPanNoMatch.isChecked -> PaymentCardSetting.InvalidCard
            else -> PaymentCardSetting.Off
        }

        return IdMethodSettings(passportEnabled, idDocumentEnabled, paymentCardSetting)
    }

}