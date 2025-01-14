package ru.calendar.feature.calendar.ui.header

class HeaderCalendarItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val id: String,
        val monthCalendar: String,
        val onClickAdded: (() -> Unit)? = null,
        val onClickExcluded: (() -> Unit)? = null
    )
}