package com.opentech.automatid.demoapp.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams


fun View.applySystemWindowInsetsToMargin(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            if (left) leftMargin = systemBars.left
            if (top) topMargin = systemBars.top
            if (right) rightMargin = systemBars.right
            if (bottom) bottomMargin = systemBars.bottom
        }
        insets
    }
}
