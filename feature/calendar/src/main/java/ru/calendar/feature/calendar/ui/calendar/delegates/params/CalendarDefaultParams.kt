package ru.calendar.feature.calendar.ui.calendar.delegates.params

import ru.calendar.core.tools.dimension.DimensionValue

data class CalendarDefaultParams(
    val stepWidth: Float,
    val stepHeight: Float,
    val cellWidth: Float,
    val cellHeight: Float,
    val indentDayOfWeekToDayOfMonth: Int,
    val containerPaddingBottom: Int,
    val containerRadius: DimensionValue.Dp,
)