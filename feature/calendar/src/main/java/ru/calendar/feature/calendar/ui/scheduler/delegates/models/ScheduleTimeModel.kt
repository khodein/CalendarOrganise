package ru.calendar.feature.calendar.ui.scheduler.delegates.models

import ru.calendar.core.tools.formatter.LocalDateFormatter

data class ScheduleTimeModel(
    val time: Time,
    val line: Line,
    val timeFormatter: LocalDateFormatter,
    val format: String,
) {
    data class Time(
        val x: Float,
        val y: Float,
    )

    data class Line(
        val x: Float,
        val y: Float
    )
}