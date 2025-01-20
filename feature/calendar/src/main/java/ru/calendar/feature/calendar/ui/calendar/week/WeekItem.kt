package ru.calendar.feature.calendar.ui.calendar.week

import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView

class WeekItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val id: String,
        val width: Int,
        val height: Int,
        val calendarDaysOfWeekDelegateView: CalendarDaysOfWeekDelegateView,
        val calendarWeekDelegateView: CalendarWeekDelegateView,
    )
}