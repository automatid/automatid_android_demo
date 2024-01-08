package com.opentech.automatid.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.opentech.automatid.demoapp.databinding.FragmentFeedbackErrorBinding

class FeedbackErrorFragment : Fragment() {
    private val args: FeedbackErrorFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFeedbackErrorBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.feedbackText.text = args.result.toString()

        return binding.root
    }

}
