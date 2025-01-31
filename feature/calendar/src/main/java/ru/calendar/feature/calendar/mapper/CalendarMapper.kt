package ru.calendar.feature.calendar.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.CalendarItem
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

interface CalendarMapper {

    fun mapCalendar(
        date: LocalDateFormatter,
        focus: LocalDateFormatter? = null,
        isAnimate: Boolean,
        isMonth: Boolean,
        provider: CalendarProvider,
    ): CalendarItem.State

    fun mapHeader(
        calendarMonth: String,
        provider: HeaderMapperProvider,
    ): HeaderCalendarItem.State

    fun mapChangeDateAlert(
        date: LocalDateFormatter,
        provider: ChangeDateAlertMapperProvider
    ): DateCarouselPickerItem.State

    interface CalendarProvider {
        fun onClickCalendarFocus(focus: LocalDateFormatter)
    }

    interface HeaderMapperProvider {
        fun onClickHeaderAdded()
        fun onClickHeaderExcluded()
        fun onClickHeaderTitle()
    }

    interface ChangeDateAlertMapperProvider {
        fun onChangeAlertDate(date: LocalDateFormatter)
    }
}