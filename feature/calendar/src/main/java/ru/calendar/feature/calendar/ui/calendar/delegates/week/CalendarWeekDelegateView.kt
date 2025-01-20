package ru.calendar.feature.calendar.ui.calendar.delegates.week

import android.graphics.Canvas
import android.view.MotionEvent
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter

interface CalendarWeekDelegateView {

    fun update(
        startDayOfWeek: LocalDateFormatter,
        month: Month,
        focus: LocalDateFormatter? = null,
    )

    fun onTouchEvent(event: MotionEvent?)

    fun getHeight(): Int

    fun draw(canvas: Canvas)
}