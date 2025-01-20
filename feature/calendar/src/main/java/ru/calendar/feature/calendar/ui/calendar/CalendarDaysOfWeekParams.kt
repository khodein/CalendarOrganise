package ru.calendar.feature.calendar.ui.calendar

import android.graphics.Typeface
import androidx.annotation.ColorInt

data class CalendarDaysOfWeekParams(
    val text: Text,
    val step: Step,
    val cell: Cell,
    val width: Int,
) {
    data class Text(
        @ColorInt val colorInt: Int,
        val size: Float,
        val typeface: Typeface?,
    )

    data class Cell(
        val width: Float
    )

    data class Step(
        val width: Float
    )
}