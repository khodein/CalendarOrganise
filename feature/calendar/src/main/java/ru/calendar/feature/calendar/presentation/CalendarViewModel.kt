package ru.calendar.feature.calendar.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.calendar.core.base.BaseViewModel
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.mapper.CalendarMapper
import ru.calendar.feature.calendar.ui.calendar.CalendarItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem
import ru.calendar.navigator.Navigator
import java.time.YearMonth
import java.util.Locale

class CalendarViewModel(
    private val calendarMapper: CalendarMapper,
    private val navigator: Navigator,
) : BaseViewModel(), CalendarMapper.CalendarProvider {

    private val _headerCalendarFlow = MutableStateFlow<HeaderCalendarItem.State?>(null)
    val headerCalendarFlow = _headerCalendarFlow.asStateFlow()

    private val _calendarFlow = MutableStateFlow<CalendarItem.State?>(null)
    val calendarFlow = _calendarFlow.asStateFlow()

    private var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault()
    private var focus: LocalDateFormatter? = null

    init {
        updateHeaderCalendar()
        updateCalendar()
    }

    private fun updateHeaderCalendar() {
        val calendarMonth = "${
            date.month.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else {
                    it.toString()
                }
            }
        } ${date.year}"

        _headerCalendarFlow.value = calendarMapper.mapHeader(
            calendarMonth = calendarMonth,
            provider = this
        )
    }

    private fun updateCalendar(
        isMonth: Boolean = true,
        isAnimate: Boolean = false,
    ) {
        _calendarFlow.value = CalendarItem.State(
            date = date,
            focus = focus,
            isAnimate = isAnimate,
            isMonth = isMonth,
            onClickFocus = ::onClickFocus
        )
    }

    private fun onClickFocus(focus: LocalDateFormatter) {
        this.focus = focus
    }

    override fun onClickAdded() {

    }

    override fun onClickExcluded() {
        val isMonth = calendarFlow.value?.isMonth ?: return
        updateCalendar(
            isMonth = !isMonth,
            isAnimate = true
        )
    }
}