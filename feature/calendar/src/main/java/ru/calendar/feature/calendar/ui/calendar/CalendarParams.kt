package ru.calendar.feature.calendar.ui.calendar

import android.graphics.Typeface
import androidx.annotation.ColorInt
import ru.calendar.core.tools.formatter.LocalDateFormatter

class CalendarParams(
    val width: Int,
    val today: LocalDateFormatter,
    val startY: Int,
    val step: Step,
    val cell: Cell,
    val text: Text,
    val focusRadius: FocusRadius,
    val todayRadius: Float,
    val backgroundCell: BackgroundCell,
) {
    data class Text(
        @ColorInt val noInMonthColorInt: Int,
        @ColorInt val inActiveColorInt: Int,
        @ColorInt val focusColorInt: Int,
        @ColorInt val todayColorInt: Int,
        val textSize: Float,
        val typeface: Typeface?,
    )

    data class BackgroundCell(
        @ColorInt val todayColorInt: Int,
        @ColorInt val focusColorInt: Int,
        @ColorInt val emptyColorInt: Int
    )

    data class Cell(
        val width: Float,
        val height: Float
    )

    data class Step(
        val width: Float,
        val height: Float
    )

    data class FocusRadius(
        val start: Float,
        val end: Float,
    )
}