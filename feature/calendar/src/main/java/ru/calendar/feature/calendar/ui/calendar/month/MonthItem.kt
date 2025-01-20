package ru.calendar.feature.calendar.ui.calendar.month

import ru.calendar.core.tools.formatter.LocalDateFormatter

class MonthItem {

    interface View {
        fun bindState(state: State)
        fun getMonthHeight(): Int
    }

    data class State(
        val focus: LocalDateFormatter? = null,
        val date: LocalDateFormatter,
        val onClickFocus: ((focus: LocalDateFormatter) -> Unit)? = null
    )
}