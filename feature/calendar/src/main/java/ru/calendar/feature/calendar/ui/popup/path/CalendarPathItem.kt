package ru.calendar.feature.calendar.ui.popup.path

import android.content.Context
import android.view.View
import kotlinx.datetime.Month
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.feature.calendar.ui.calendar.CalendarItem

class CalendarPathItem {

    interface View {
        fun bindState(state: State)
    }

    data class State(
        val id: String,
        val text: String,
        val data: Data,
        var isFocus: Boolean = false,
        val onClick: ((data: Data) -> Unit)? = null,
    ): RecyclerState {
        override val provideId: String = id
        override val viewType: Int = CalendarPathItem::class.hashCode()
        override fun getView(context: Context) = CalendarPathItemView(context)
    }

    sealed interface Data {
        data class MonthCondition(val month: Month): Data
        data class YearCondition(val year: Int): Data
    }
}