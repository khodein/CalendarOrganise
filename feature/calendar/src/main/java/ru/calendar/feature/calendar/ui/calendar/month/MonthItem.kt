package ru.calendar.feature.calendar.ui.calendar.month

import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateView

class MonthItem {

    interface View {
        fun bindState(state: State)
        fun onUpdateView()
    }

    data class State(
        val id: String,
        val daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView,
        val monthDelegateView: CalendarMonthDelegateView,
    )
}