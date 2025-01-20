package ru.calendar.feature.calendar.ui.calendar.week

import android.content.Context
import android.view.View
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.tools.formatter.LocalDateFormatter
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
        val startDayOfWeek: LocalDateFormatter,
        val calendarDaysOfWeekDelegateView: CalendarDaysOfWeekDelegateView,
        val calendarWeekDelegateView: CalendarWeekDelegateView,
    ): RecyclerState {
        override val provideId: String = id
        override val viewType: Int = WeekItem::class.hashCode()
        override fun getView(context: Context) = WeekItemView(context)
    }
}