package ru.calendar.feature.calendar.ui.date_carousel

import ru.calendar.core.tools.formatter.LocalDateFormatter

class DateCarouselPickerItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val focus: LocalDateFormatter,
        val onChangeDate: ((year: Int, month: String) -> Unit)? = null
    )
}