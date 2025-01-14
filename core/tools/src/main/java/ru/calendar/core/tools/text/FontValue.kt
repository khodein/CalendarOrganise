package ru.calendar.core.tools.text

import androidx.annotation.FontRes
import ru.calendar.core.res.R as resR

enum class FontValue(
    @FontRes val fontResId: Int
) {
    BOLD(resR.font.bold),
    REGULAR(resR.font.regular),
    EXTRABOLD(resR.font.extrabold),
}