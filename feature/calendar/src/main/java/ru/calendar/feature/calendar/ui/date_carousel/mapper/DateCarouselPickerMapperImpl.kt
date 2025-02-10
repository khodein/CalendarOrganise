package ru.calendar.feature.calendar.ui.date_carousel.mapper

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.model.DatePickerBuilderModel

class DateCarouselPickerMapperImpl : DateCarouselPickerMapper {

    private val dateNow by lazy {
        LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    }

    private val monthList = Month.entries.map { it.name }

    override fun mapMonthList(focus: LocalDateFormatter): DatePickerBuilderModel {
        return DatePickerBuilderModel(
            list = monthList.toTypedArray(),
            focusIndex = monthList.indexOf(focus.month.name)
        )
    }

    override fun mapYearList(focus: LocalDateFormatter): DatePickerBuilderModel {
        var year = dateNow.year - 3
        var focusIndex = 0
        val list = buildList<String>(YEARS_COUNT) {
            repeat(YEARS_COUNT) { count ->
                add(year.toString())
                if (year == focus.year) {
                    focusIndex = count
                }
                year += 1
            }
        }

        return DatePickerBuilderModel(
            list = list.toTypedArray(),
            focusIndex = focusIndex,
        )
    }

    override fun mapResult(
        year: Int,
        month: String,
        default: LocalDateFormatter,
    ): LocalDateFormatter {
        val newMonth = Month.entries.firstOrNull { it.name == month }
        val localDateTime = LocalDateTime(
            year = year,
            month = newMonth ?: default.month,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
        )
        return LocalDateFormatter(localDateTime)
    }

    private companion object {
        const val YEARS_COUNT = 7
    }
}