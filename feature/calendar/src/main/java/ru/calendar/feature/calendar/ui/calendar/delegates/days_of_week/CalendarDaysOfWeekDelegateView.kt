package ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week

import android.graphics.Canvas
import android.view.MotionEvent
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.formatter.LocalDateFormatter

interface CalendarDaysOfWeekDelegateView {

    fun draw(canvas: Canvas)

    fun getHeight(): Int

    data class Params(
        val textSize: DimensionValue.SpToPx,
        val textColor: ColorValue,
        val cellWidth: DimensionValue.Dp,
        val stepWidth: DimensionValue.Dp,
        val width: Int,
    )
}