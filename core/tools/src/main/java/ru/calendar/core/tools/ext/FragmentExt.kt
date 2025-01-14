package ru.calendar.core.tools.ext

import androidx.fragment.app.Fragment

fun Fragment.showKeyboard() = toggleKeyboard(true)
fun Fragment.hideKeyboard() = toggleKeyboard(false)

private fun Fragment.toggleKeyboard(isShow: Boolean) {
    val activity = this.activity
    activity ?: return
    activity.toggleKeyboard(isShow)
}