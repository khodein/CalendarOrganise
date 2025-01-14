package ru.calendar.core.tools.ext

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.showKeyboard() = toggleKeyboard(true)
fun Activity.hideKeyboard() = toggleKeyboard(false)

internal fun Activity.toggleKeyboard(isShow: Boolean) {
    val currentFocusedView = this.currentFocus
    currentFocusedView ?: return
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    when (isShow) {
        true -> {
            inputMethodManager.showSoftInput(currentFocusedView, InputMethodManager.SHOW_IMPLICIT)
        }
        false -> {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            currentFocusedView.clearFocus()
        }
    }
}