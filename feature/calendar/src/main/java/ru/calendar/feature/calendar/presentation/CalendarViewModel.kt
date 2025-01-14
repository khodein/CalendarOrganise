package ru.calendar.feature.calendar.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.calendar.core.base.BaseViewModel
import ru.calendar.feature.calendar.mapper.CalendarMapper
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem
import ru.calendar.navigator.Navigator

class CalendarViewModel(
    private val calendarMapper: CalendarMapper,
    private val navigator: Navigator,
) : BaseViewModel(), CalendarMapper.CalendarProvider {

    private val _headerCalendarFlow = MutableStateFlow<HeaderCalendarItem.State?>(null)
    val headerCalendarFlow = _headerCalendarFlow.asStateFlow()

    init {
        updateHeaderCalendar()
    }

    private fun updateHeaderCalendar() {
        _headerCalendarFlow.value = calendarMapper.mapHeader(
            calendarMonth = "January 2024",
            provider = this
        )
    }

    override fun onClickAdded() {

    }

    override fun onClickExcluded() {

    }
}