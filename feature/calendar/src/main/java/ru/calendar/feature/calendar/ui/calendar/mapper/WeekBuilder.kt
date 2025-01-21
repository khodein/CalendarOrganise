package ru.calendar.feature.calendar.ui.calendar.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams
import ru.calendar.feature.calendar.ui.calendar.week.WeekItem

class WeekBuilder(
    val weekList: List<WeekItem.State>,
    val height: Int,
    val lastCountWeekFocus: Int,
) {
}