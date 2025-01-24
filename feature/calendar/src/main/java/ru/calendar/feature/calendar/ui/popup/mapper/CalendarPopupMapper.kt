package ru.calendar.feature.calendar.ui.popup.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.popup.path.CalendarPathItem

interface CalendarPopupMapper {
    fun buildYearsList(
        focus: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault(),
        onClickCalendarPath: ((data: CalendarPathItem.Data) -> Unit)? = null
    ): List<CalendarPathItem.State>

    fun buildMonthsList(
        focus: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault(),
        onClickCalendarPath: ((data: CalendarPathItem.Data) -> Unit)? = null
    ): List<CalendarPathItem.State>
}