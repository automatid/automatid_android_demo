package com.opentech.automatid.demoapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.opentech.automatid.AutomatIdIdentificationResult
import com.opentech.automatid.demoapp.databinding.FragmentFeedbackBinding

class FeedbackFragment : Fragment() {
    private val args: FeedbackFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        when (val result = args.result) {
            is AutomatIdIdentificationResult.Success -> {
                binding.logo.setAnimation(R.raw.automatid_demo_app_feedback_success)
                binding.feedbackTitle.setText(R.string.feedback_title_success)
                binding.feedbackText.setText(R.string.feedback_body_success)
                binding.btnShareJwt.isGone = false
                binding.btnShareJwt.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, result.jwt)
                        startActivity(Intent.createChooser(intent, null))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            is AutomatIdIdentificationResult.Error -> {
                binding.logo.setAnimation(R.raw.automatid_demo_app_feedback_error)
                binding.feedbackTitle.setText(R.string.feedback_title_error)
                binding.feedbackText.text = result.toString()
                binding.btnShareJwt.isGone = true
            }
        }

        return binding.root
    }

}
