package com.opentech.automatid.demoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.opentech.automatid.demoapp.databinding.FragmentTcBinding

class TcFragment : Fragment() {
    private val args: TcFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTcBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.webview.apply {
            isInvisible = true  // prevents white blinking while loading
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        isInvisible = false  // prevents white blinking while loading
                        binding.progressIndicator.isInvisible = true
                        binding.btnAccept.isEnabled = true
                    }
                }
            }
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl("https://docs.automat-id.com/privacy.html")
        }

        binding.btnAccept.apply {
            isEnabled = false
            isGone = !args.showAcceptButton
            setOnClickListener {
                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set(TC_ACCEPTED_WHILE_STARTING_EXTRA, true)
                navController.popBackStack()
            }
        }

        return binding.root
    }

    companion object {
        const val TC_ACCEPTED_WHILE_STARTING_EXTRA = "tcAccepted"
    }
}
