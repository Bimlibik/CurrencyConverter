package com.foxy.currencyconverter.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.foxy.currencyconverter.Event
import com.foxy.currencyconverter.R
import com.google.android.material.snackbar.Snackbar

/**
 *  Extension that triggers a snackbar message when the value contained by snackbarEvent is modified.
 */

fun View.showSnackbar(message: String, btnText: String, listener: View.OnClickListener) {
    Snackbar
        .make(this, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(btnText, listener)
        .show()
}


fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
) {

    snackbarEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(
                context.getString(it),
                context.getString(R.string.snackbar_action_ok)
            ) { }
        }
    })
}