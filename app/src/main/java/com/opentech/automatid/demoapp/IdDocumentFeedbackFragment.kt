package com.opentech.automatid.demoapp

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.opentech.automatid.demoapp.databinding.FragmentFeedbackIdDocBinding
import org.json.JSONObject

class IdDocumentFeedbackFragment : Fragment() {
    private val args: IdDocumentFeedbackFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFeedbackIdDocBinding.inflate(inflater, container, false)

        // WARNING: the following code performs local parsing of the JWT *without validating it*. THIS IS FOR DEMO PURPOSES ONLY!
        // In a real app, you should simply send the JWT to your server, validate it, and perform the parsing operations on the server.
        val matchPercentage = runCatching {
            val payload = args.result.jwt.split(".")[1]
            val jsonStr = Base64.decode(payload, Base64.URL_SAFE).decodeToString()
            val perc = JSONObject(jsonStr).getDouble("photoMatchingResult")
            perc.toInt().toString() + "%"
        }.getOrDefault("N/A")
        binding.check4Value.text = matchPercentage

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
