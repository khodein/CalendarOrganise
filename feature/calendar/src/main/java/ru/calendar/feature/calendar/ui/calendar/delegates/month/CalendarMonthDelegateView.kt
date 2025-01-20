package ru.calendar.feature.calendar.ui.calendar.delegates.month

import android.graphics.Canvas
import android.view.MotionEvent
import ru.calendar.core.tools.formatter.LocalDateFormatter

interface CalendarMonthDelegateView {

    fun draw(canvas: Canvas)

    fun update(
        date: LocalDateFormatter,
        focus: LocalDateFormatter? = null,
    )

    fun onTouchEvent(event: MotionEvent?, onInvalidate: () -> Unit)

    fun getHeight(): Int
}