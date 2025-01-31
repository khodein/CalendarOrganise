package ru.calendar.feature.calendar.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import ru.calendar.core.base.BaseViewModel
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.mapper.CalendarMapper
import ru.calendar.feature.calendar.ui.calendar.CalendarItem
import ru.calendar.feature.calendar.ui.date_carousel.DateCarouselPickerItem
import ru.calendar.feature.calendar.ui.header.HeaderCalendarItem
import ru.calendar.navigator.Navigator
import java.util.Locale

class CalendarViewModel(
    private val calendarMapper: CalendarMapper,
    private val navigator: Navigator,
) : BaseViewModel(),
    CalendarMapper.HeaderMapperProvider,
    CalendarMapper.ChangeDateAlertMapperProvider {
    private val _headerCalendarFlow = MutableStateFlow<HeaderCalendarItem.State?>(null)
    val headerCalendarFlow = _headerCalendarFlow.asStateFlow()

    private val _calendarFlow = MutableStateFlow<CalendarItem.State?>(null)
    val calendarFlow = _calendarFlow.asStateFlow()

    private val _showAlertChangeDateFlow =
        MutableSharedFlow<DateCarouselPickerItem.State?>(replay = 1)
    val showAlertChangeDateFlow = _showAlertChangeDateFlow.asSharedFlow()

    private var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    private var focus: LocalDateFormatter? = null

    init {
        updateState()
    }

    private fun updateState() {
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
        isMonth: Boolean = calendarFlow.value?.isMonth ?: true,
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

    override fun onClickHeaderAdded() {

    }

    override fun onClickHeaderExcluded() {
        val isMonth = calendarFlow.value?.isMonth ?: return
        updateCalendar(
            isMonth = !isMonth,
            isAnimate = true
        )
    }

    override fun onClickHeaderTitle() {
        val state = calendarMapper.mapChangeDateAlert(
            date = date,
            provider = this
        )
        _showAlertChangeDateFlow.tryEmit(state)
    }

    override fun onClickDate(year: Int, month: String) {
        val localDateTime = LocalDateTime(
            year = year,
            month = Month.entries.firstOrNull { it.name == month } ?: date.month,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        )
        date = LocalDateFormatter(localDateTime)
        updateState()
    }
}