package com.opentech.automatid.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.opentech.automatid.demoapp.databinding.FragmentSettingsBinding
import com.opentech.automatid.demoapp.settings.PaymentCardSetting
import com.opentech.automatid.demoapp.settings.SettingsViewModel

class SettingsFragment : Fragment() {
    private val settings: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnIdMethods.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.settingsToAuthenticationMethods())
        }

        binding.btnErrorMgmt.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.settingsToErrorMgmt())
        }

        settings.idMethodSettingsLiveData.observe(viewLifecycleOwner) { state ->
            val enabledIdentificationOptions = buildList {
                if (state.passportEnabled) {
                    add(getString(R.string.settings_selection_passport))
                }
                if (state.idDocumentEnabled) {
                    add(getString(R.string.settings_selection_identity_card))
                }
                when (state.paymentCardSetting) {
                    PaymentCardSetting.Off -> {}
                    PaymentCardSetting.ValidCard -> add(getString(R.string.settings_selection_payment_card_valid))
                    PaymentCardSetting.InvalidCard -> add(getString(R.string.settings_selection_payment_card_invalid))
                }
            }
            binding.idMethodsValue.text = enabledIdentificationOptions.joinToString(", ")
        }

        settings.retryOnErrorsLiveData.observe(viewLifecycleOwner) { retryOnErrors ->
            binding.errorMgmtValue.text = if (retryOnErrors) getString(R.string.settings_selection_retry_on_errors) else getString(R.string.settings_selection_abort_on_errors)
        }

        return binding.root
    }
}
