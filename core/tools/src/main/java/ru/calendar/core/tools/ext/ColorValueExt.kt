package ru.calendar.core.tools.ext

import android.content.Context
import androidx.annotation.ColorInt
import ru.calendar.core.tools.color.ColorValue

@ColorInt
fun ColorValue.getColor(context: Context): Int {
    return when(this) {
        is ColorValue.Color -> this.value
        is ColorValue.Res -> context.getColorCompat(this.value)
    }
}