package ru.calendar.feature.calendar.ui.calendar.delegates.week

import android.graphics.Canvas
import android.view.MotionEvent
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

interface CalendarWeekDelegateView {

    fun update(
        startDayOfWeek: LocalDateFormatter,
        month: Month,
        focus: LocalDateFormatter? = null,
        count: Int,
        params: CalendarParams?,
    )

    fun onTouchEvent(event: MotionEvent?, onInvalidate: () -> Unit)

    fun getHeight(): Int

    fun draw(canvas: Canvas)
}