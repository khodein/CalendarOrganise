package ru.calendar.feature.calendar.ui.calendar.delegates

import android.graphics.RectF
import androidx.annotation.ColorInt
import ru.calendar.core.tools.formatter.LocalDateFormatter

data class CalendarDay(
    val count: Int,
    val x: Float,
    val y: Float,
    val date: LocalDateFormatter,
    val focus: Focus,
    val today: Today,
    val coordinateTap: CoordinateTap,
    @ColorInt val textColorInt: Int,
    @ColorInt val backgroundColorInt: Int
) {
    val text: String
        get() = date.dayOfMonth.toString()

    data class CoordinateTap(
        val top: Float,
        val bottom: Float,
        val left: Float,
        val right: Float,
    )

    data class Today(
        val isVisible: Boolean,
        val rectF: RectF,
    )

    data class Focus(
        val isVisible: Boolean,
        val x: Float,
        val y: Float,
    )
}