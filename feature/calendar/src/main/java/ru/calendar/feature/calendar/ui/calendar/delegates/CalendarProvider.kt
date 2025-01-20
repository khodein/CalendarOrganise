package ru.calendar.feature.calendar.ui.calendar.delegates

import ru.calendar.core.tools.formatter.LocalDateFormatter

interface CalendarProvider {
    fun onClickFocus(focus: LocalDateFormatter)
    fun onUpdateView(type: CalendarType)
}