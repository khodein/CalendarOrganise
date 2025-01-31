package ru.calendar.feature.calendar.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.CalendarItem
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

class CalendarMapperImpl : CalendarMapper {

    override fun mapCalendar(
        date: LocalDateFormatter,
        focus: LocalDateFormatter?,
        isAnimate: Boolean,
        isMonth: Boolean,
        provider: CalendarMapper.CalendarProvider
    ): CalendarItem.State {
        return CalendarItem.State(
            date = date,
            focus = focus,
            isAnimate = isAnimate,
            isMonth = isMonth,
            onClickFocus = provider::onClickCalendarFocus
        )
    }

    override fun mapHeader(
        calendarMonth: String,
        provider: CalendarMapper.HeaderMapperProvider
    ): HeaderCalendarItem.State {
        return HeaderCalendarItem.State(
            id = "calendar_header_id",
            monthCalendar = calendarMonth,
            onClickExcluded = provider::onClickHeaderExcluded,
            onClickAdded = provider::onClickHeaderAdded,
            onClickTitle = provider::onClickHeaderTitle
        )
    }

    override fun mapChangeDateAlert(
        date: LocalDateFormatter,
        provider: CalendarMapper.ChangeDateAlertMapperProvider
    ): DateCarouselPickerItem.State {
        return DateCarouselPickerItem.State(
            focus = date,
            onChangeDate = provider::onChangeAlertDate
        )
    }
}