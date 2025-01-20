package ru.calendar.feature.calendar.ui.calendar

import ru.calendar.core.tools.formatter.LocalDateFormatter

class CalendarItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val id: String,
        val isMonth: Boolean,
        val month: LocalDateFormatter,
        var focus: LocalDateFormatter? = null,
        val onClickFocus: ((date: LocalDateFormatter) -> Unit)? = null
    )
}