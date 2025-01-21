package ru.calendar.feature.calendar.ui.calendar

import ru.calendar.core.tools.formatter.LocalDateFormatter

class CalendarItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val isMonth: Boolean = true,
        var isAnimate: Boolean = false,
        val date: LocalDateFormatter,
        var focus: LocalDateFormatter? = null,
        val onClickFocus: ((date: LocalDateFormatter) -> Unit)? = null
    )
}