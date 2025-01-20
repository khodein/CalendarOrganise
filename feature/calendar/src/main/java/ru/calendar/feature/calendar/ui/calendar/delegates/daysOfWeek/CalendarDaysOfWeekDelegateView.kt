package ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek

import android.graphics.Canvas

interface CalendarDaysOfWeekDelegateView {

    fun draw(canvas: Canvas)

    fun update()

    fun getHeight(): Int
}