package ru.calendar.feature.calendar.ui.popup

import ru.calendar.core.tools.formatter.LocalDateFormatter

class CalendarPopupItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        var date: LocalDateFormatter,
        val type: Type,
    )

    enum class Type {
        MONTH_YEAR,
        WEEK
    }
}