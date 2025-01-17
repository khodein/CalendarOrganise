package ru.calendar.core.tools.ext

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Context.getFont(@FontRes id: Int) = ResourcesCompat.getFont(this, id)