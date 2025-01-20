package ru.calendar.feature.calendar.ui.calendar.week

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.size.SizeValue
import ru.calendar.feature.calendar.ui.calendar.day.Day
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView

class WeekItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val id: String,
        val sizeValue: SizeValue,
        val calendarDaysOfWeekDelegateView: CalendarDaysOfWeekDelegateView,
        val calendarWeekDelegateView: CalendarWeekDelegateView,
    )
}