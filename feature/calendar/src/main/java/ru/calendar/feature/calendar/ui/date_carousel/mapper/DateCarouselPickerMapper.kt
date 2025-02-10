package ru.calendar.feature.calendar.ui.date_carousel.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.model.DatePickerBuilderModel

interface DateCarouselPickerMapper {

    fun mapMonthList(focus: LocalDateFormatter): DatePickerBuilderModel

    fun mapYearList(focus: LocalDateFormatter): DatePickerBuilderModel

    fun mapResult(
        year: Int,
        month: String,
        default: LocalDateFormatter,
    ): LocalDateFormatter
}