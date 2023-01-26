package com.opentech.automatid.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.opentech.automatid.demoapp.databinding.ActivityMainBinding
import com.opentech.automatid.demoapp.util.applySystemWindowInsetsToMargin


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.applySystemWindowInsetsToMargin(top = true, bottom = true)
    }
}
