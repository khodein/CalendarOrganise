package ru.calendar.feature.calendar.ui.calendar.model

import ru.calendar.feature.calendar.ui.calendar.week.WeekItem

class WeekBuilderModel(
    val weekList: List<WeekItem.State>,
    val height: Int,
    val lastCountWeekFocus: Int,
) {
}