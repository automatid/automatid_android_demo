package com.opentech.automatid.demoapp.util

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheetDialogFragment that is always in EXPANDED state.
 */
abstract class Sheet : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext()).apply {
            setOnShowListener {
                val behavior = (dialog as BottomSheetDialog).behavior
                behavior.skipCollapsed = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    fun setCloseable(closeable: Boolean) {
        dialog?.setCanceledOnTouchOutside(closeable)
        dialog?.setCancelable(closeable)
    }
}
