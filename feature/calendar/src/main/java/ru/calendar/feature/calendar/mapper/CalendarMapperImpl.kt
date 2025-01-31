package ru.calendar.feature.calendar.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

class CalendarMapperImpl : CalendarMapper {

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
            onChangeDate = provider::onClickDate
        )
    }
}