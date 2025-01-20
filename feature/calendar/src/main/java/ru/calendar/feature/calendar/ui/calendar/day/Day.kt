package ru.calendar.feature.calendar.ui.calendar.day

import androidx.annotation.ColorInt
import ru.calendar.core.tools.formatter.LocalDateFormatter

data class Day(
    val count: Int,
    val x: Float,
    val y: Float,
    val text: String,
    val date: LocalDateFormatter,
    val isFocus: Boolean,
    val isToday: Boolean,
    val coordinate: Coordinate,
    @ColorInt val textColorInt: Int,
    @ColorInt val backgroundColorInt: Int
) {
    data class Coordinate(
        val top: Float,
        val bottom: Float,
        val left: Float,
        val right: Float,
    )
}