package ru.calendar.feature.calendar.ui.date_carousel.mapper

import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.date_carousel.model.DatePickerBuilder

class DateCarouselPickerMapperImpl : DateCarouselPickerMapper {

    private val dateNow by lazy {
        LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    }

    private val monthList = Month.entries.map { it.name }

    override fun mapMonthList(focus: LocalDateFormatter): DatePickerBuilder {
        return DatePickerBuilder(
            list = monthList.toTypedArray(),
            focusIndex = monthList.indexOf(focus.month.name)
        )
    }

    override fun mapYearList(focus: LocalDateFormatter): DatePickerBuilder {
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

        return DatePickerBuilder(
            list = list.toTypedArray(),
            focusIndex = focusIndex,
        )
    }

    private companion object {
        const val YEARS_COUNT = 7
    }
}