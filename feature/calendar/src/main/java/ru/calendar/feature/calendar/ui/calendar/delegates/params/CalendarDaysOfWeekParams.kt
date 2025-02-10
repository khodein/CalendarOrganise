package ru.calendar.feature.calendar.ui.calendar.delegates.params

import android.graphics.Typeface
import androidx.annotation.ColorInt

data class CalendarDaysOfWeekParams(
    val text: Text,
    val step: Step,
    val cell: Cell,
    val width: Int,
) {
    class Text(
        @ColorInt val colorInt: Int,
        val size: Float,
        val typeface: Typeface?,
    )

    class Cell(
        val width: Float
    )

    class Step(
        val width: Float
    )
}