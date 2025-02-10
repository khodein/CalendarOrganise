package ru.calendar.feature.calendar.ui.scheduler.delegates.time

import android.graphics.Canvas
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.formatter.LocalDateFormatter

interface ScheduleTimeDelegateView {
    fun update(
        rootWidth: Int,
        focusDay: LocalDateFormatter,
        startX: DimensionValue.Dp,
        startY: DimensionValue.Dp,
    )
    fun draw(canvas: Canvas, offsetX: Float, offsetY: Float)
    fun getHeight(): Float
}