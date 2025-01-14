package ru.calendar.core.tools.text

import androidx.annotation.StyleRes
import ru.calendar.core.tools.color.ColorValue

sealed interface TextStyleValue {
    data class Res(@StyleRes val value: Int) : TextStyleValue
    data class Custom(
        val color: ColorValue,
        val size: Float,
        val fontStyle: FontValue
    ) : TextStyleValue
}