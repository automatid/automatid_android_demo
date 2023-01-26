package com.opentech.automatid.demoapp

import android.app.Application
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.opentech.automatid.AutomatIdConfiguration
import com.opentech.automatid.AutomatIdInitResult
import com.opentech.automatid.AutomatIdManager


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val configFile = "config.json"

        val configuration = AutomatIdConfiguration.Builder()
            .withDefaultFontRegular(ResourcesCompat.getFont(this, R.font.montserrat_regular))
            .withDefaultFontMedium(ResourcesCompat.getFont(this, R.font.montserrat_medium))
            .withDefaultFontBold(ResourcesCompat.getFont(this, R.font.montserrat_bold))
            .withTitleFontBold(ResourcesCompat.getFont(this, R.font.montserrat_bold))
            .withBtnFontRegular(ResourcesCompat.getFont(this, R.font.montserrat_regular))
            .withBtnFontMedium(ResourcesCompat.getFont(this, R.font.montserrat_medium))
            .withBtnFontBold(ResourcesCompat.getFont(this, R.font.montserrat_bold))
            .build()

        val initResult = AutomatIdManager.init(this, configFile, configuration)
        if (initResult is AutomatIdInitResult.Error) {
            Log.e("SCI", "SciManager init error: $initResult")
        }
    }

}
