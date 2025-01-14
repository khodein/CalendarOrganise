package ru.calendar.feature.calendar.mapper

import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem

class CalendarMapperImpl : CalendarMapper {

    override fun mapHeader(
        calendarMonth: String,
        provider: CalendarMapper.CalendarProvider
    ): HeaderCalendarItem.State {
        return HeaderCalendarItem.State(
            id = "calendar_header_id",
            monthCalendar = calendarMonth,
            onClickExcluded = provider::onClickExcluded,
            onClickAdded = provider::onClickAdded
        )
    }


}