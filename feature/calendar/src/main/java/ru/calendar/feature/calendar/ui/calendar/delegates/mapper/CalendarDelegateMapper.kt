package ru.calendar.feature.calendar.ui.calendar.delegates.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarDay
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

interface CalendarDelegateMapper {
    fun mapDay(
        count: Int,
        x: Float,
        y: Float,
        focusDate: LocalDateFormatter? = null,
        date: LocalDateFormatter,
        isDaysBefore: Boolean,
        isDaysAfter: Boolean,
        cellHeight: Float,
        params: CalendarParams,
    ): CalendarDay
}