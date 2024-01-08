package com.opentech.automatid.demoapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.opentech.automatid.AutomatIdCardScanInput
import com.opentech.automatid.AutomatIdDocumentType.IDENTITY_CARD
import com.opentech.automatid.AutomatIdDocumentType.PASSPORT
import com.opentech.automatid.AutomatIdDocumentType.PAYMENT_CARD
import com.opentech.automatid.AutomatIdIdentificationRequest
import com.opentech.automatid.AutomatIdIdentificationResult
import com.opentech.automatid.AutomatIdLifecycleCallback
import com.opentech.automatid.AutomatIdManager
import com.opentech.automatid.ErrorHandlingDecision
import com.opentech.automatid.demoapp.databinding.FragmentHomeBinding
import com.opentech.automatid.demoapp.settings.PaymentCardSetting
import com.opentech.automatid.demoapp.settings.SettingsViewModel

class HomeFragment : Fragment() {
    private val settings: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Boolean>(TcFragment.TC_ACCEPTED_WHILE_STARTING_EXTRA)?.observe(viewLifecycleOwner) { tcAccepted ->
            savedStateHandle.remove<Boolean>(TcFragment.TC_ACCEPTED_WHILE_STARTING_EXTRA)
            settings.tcAccepted = tcAccepted
            if (tcAccepted) {
                startIdentification()
            }
        }

        binding.toolbar.menu.findItem(R.id.menu_tc).setOnMenuItemClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToTc(false))
            true
        }
        binding.toolbar.menu.findItem(R.id.menu_settings).setOnMenuItemClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToSettings())
            true
        }

        binding.btnStart.setOnClickListener {
            if (settings.tcAccepted) {
                startIdentification()
            } else {
                findNavController().navigate(HomeFragmentDirections.homeToTc(true))
            }
        }

        return binding.root
    }

    private fun startIdentification() {
        val idMethodSettings = settings.idMethodSettingsLiveData.value!!

        val documentTypes = buildSet {
            if (idMethodSettings.idDocumentEnabled) {
                add(IDENTITY_CARD)
            }
            if (idMethodSettings.passportEnabled) {
                add(PASSPORT)
            }
            if (idMethodSettings.paymentCardSetting != PaymentCardSetting.Off) {
                add(PAYMENT_CARD)
            }
        }

        /*
         * NOTE:
         * In the Demo App we use an hardcoded, test JWT.
         * In your real app, you should build the JWT on your backend according to the documentation and retrieve it.
         */
        val cardScanInput = when (idMethodSettings.paymentCardSetting) {
            PaymentCardSetting.Off -> null
            PaymentCardSetting.ValidCard -> AutomatIdCardScanInput(getString(R.string.cardscan_match_jwt))
            PaymentCardSetting.InvalidCard -> AutomatIdCardScanInput(getString(R.string.cardscan_no_match_jwt))
        }

        val identificationRequest = AutomatIdIdentificationRequest.Builder()
            .withDocumentTypes(documentTypes)
            .withCardScanInput(cardScanInput)
            .withShowFallbackButton(false)
            .build()

        AutomatIdManager.startIdentification(requireActivity(), identificationRequest, object : AutomatIdLifecycleCallback(this) {
            override fun onSuccess_(result: AutomatIdIdentificationResult.Success) {
                Log.i("AutomatID", "Success: $result")
                if (result.usedDocument == PAYMENT_CARD) {
                    findNavController().navigate(HomeFragmentDirections.homeToFeedbackCardscan(result))
                } else {
                    findNavController().navigate(HomeFragmentDirections.homeToFeedbackIdDoc(result))
                }
            }

            override fun onCancel_() {
                Log.i("AutomatID", "Canceled by user")
            }

            override fun onError_(error: AutomatIdIdentificationResult.Error) {
                Log.e("AutomatID", "Error: $error")
                findNavController().navigate(HomeFragmentDirections.homeToFeedbackError(error))
            }

            override fun decideRecoverableErrorHandling(error: AutomatIdIdentificationResult.Error): ErrorHandlingDecision {
                Log.w("AutomatID", "decideRecoverableErrorHandling: $error")
                val retryOnErrors = settings.retryOnErrorsLiveData.value!!
                return if (retryOnErrors) ErrorHandlingDecision.RETRY_IDENTIFICATION else ErrorHandlingDecision.ABORT_IDENTIFICATION
            }
        })
    }

}