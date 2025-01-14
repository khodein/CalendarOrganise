package ru.calendar.feature.calendar.mapper

import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

interface CalendarMapper {
    fun mapHeader(
        calendarMonth: String,
        provider: CalendarProvider,
    ): HeaderCalendarItem.State

    interface CalendarProvider {
        fun onClickAdded()
        fun onClickExcluded()
    }
}