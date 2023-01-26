package com.opentech.automatid.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.opentech.automatid.demoapp.databinding.SheetErrorManagementBinding
import com.opentech.automatid.demoapp.settings.SettingsViewModel
import com.opentech.automatid.demoapp.util.Sheet

class SheetErrorManagement : Sheet() {
    private val settings: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = SheetErrorManagementBinding.inflate(inflater, container, false)

        settings.retryOnErrorsLiveData.observe(viewLifecycleOwner) { retryOnErrors ->
            binding.switchRetryOnErrors.isChecked = retryOnErrors
            evaluateBody2(binding, retryOnErrors)
        }

        binding.switchRetryOnErrors.setOnCheckedChangeListener { _, isChecked ->
            evaluateBody2(binding, isChecked)
        }

        binding.btnConfirm.setOnClickListener {
            settings.setRetryOnErrors(binding.switchRetryOnErrors.isChecked)
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun evaluateBody2(binding: SheetErrorManagementBinding, retryOnErrors: Boolean) {
        if (retryOnErrors) {
            binding.txtBody2.setText(R.string.error_management_body2_retry)
        } else {
            binding.txtBody2.setText(R.string.error_management_body2_abort)
        }
    }
}
