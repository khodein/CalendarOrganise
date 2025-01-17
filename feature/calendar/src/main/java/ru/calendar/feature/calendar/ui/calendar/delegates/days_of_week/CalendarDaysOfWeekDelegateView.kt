package ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week

import android.graphics.Canvas
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue

interface CalendarDaysOfWeekDelegateView {

    fun drawDaysOfWeek(canvas: Canvas)

    fun getHeight(): Int

    data class Params(
        val textSize: DimensionValue.SpToPx,
        val textColor: ColorValue,
        val width: Int,
    )
}