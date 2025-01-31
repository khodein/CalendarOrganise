package ru.calendar.feature.calendar.ui.date_carousel.mapper

import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.model.DatePickerBuilder

interface DateCarouselPickerMapper {

    fun mapMonthList(focus: LocalDateFormatter): DatePickerBuilder

    fun mapYearList(focus: LocalDateFormatter): DatePickerBuilder
}