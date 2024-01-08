package com.opentech.automatid.demoapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.opentech.automatid.demoapp.databinding.FragmentFeedbackCardBinding

class CardScanFeedbackFragment : Fragment() {
    private val args: IdDocumentFeedbackFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFeedbackCardBinding.inflate(inflater, container, false)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnShareJwt.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, args.result.jwt)
                startActivity(Intent.createChooser(intent, null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return binding.root
    }

}
