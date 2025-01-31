package ru.calendar.feature.calendar.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

interface CalendarMapper {
    fun mapHeader(
        calendarMonth: String,
        provider: HeaderMapperProvider,
    ): HeaderCalendarItem.State

    fun mapChangeDateAlert(
        date: LocalDateFormatter,
        provider: ChangeDateAlertMapperProvider
    ): DateCarouselPickerItem.State

    interface HeaderMapperProvider {
        fun onClickHeaderAdded()
        fun onClickHeaderExcluded()
        fun onClickHeaderTitle()
    }

    interface ChangeDateAlertMapperProvider {
        fun onClickDate(year: Int, month: String)
    }
}