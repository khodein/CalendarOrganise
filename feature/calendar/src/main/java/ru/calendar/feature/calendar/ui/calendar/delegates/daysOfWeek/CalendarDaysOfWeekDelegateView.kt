package ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek

import android.graphics.Canvas
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarDaysOfWeekParams

interface CalendarDaysOfWeekDelegateView {

    fun draw(canvas: Canvas)

    fun update(params: CalendarDaysOfWeekParams)

    fun getHeight(): Int
}