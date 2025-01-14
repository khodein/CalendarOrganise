package ru.calendar.core.tools.ext

import android.view.Menu
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

fun Menu.addItem(
    @IdRes groupId: Int,
    @IdRes idRes: Int,
    @StringRes nameRes: Int,
    @DrawableRes iconRes: Int,
) {
    add(
        groupId,
        idRes,
        Menu.NONE,
        nameRes
    ).setIcon(iconRes)
}