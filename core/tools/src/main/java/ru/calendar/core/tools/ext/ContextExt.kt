package ru.calendar.core.tools.ext

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)