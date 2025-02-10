package ru.calendar.feature.calendar.ui.scheduler

import ru.calendar.core.tools.formatter.LocalDateFormatter

class ScheduleItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val focusDate: LocalDateFormatter,
    )
}