package ru.calendar.feature.calendar.ui.popup.mapper

import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.popup.path.CalendarPathItem

class CalendarPopupMapperImpl : CalendarPopupMapper {

    override fun buildYearsList(
        focus: LocalDateFormatter,
        onClickCalendarPath: ((data: CalendarPathItem.Data) -> Unit)?
    ): List<CalendarPathItem.State> {
        var year = LocalDateFormatter.nowInSystemDefault().year - 2
        val years = buildList<CalendarPathItem.State>(COUNT_YEARS) {
            repeat(COUNT_YEARS) { count ->
                CalendarPathItem.State(
                    id = "${count}",
                    text = year.toString(),
                    data = CalendarPathItem.Data.YearCondition(year),
                    isFocus = year == focus.year,
                    onClick = onClickCalendarPath
                ).let(::add)
                year += 1
            }
        }
        return years
    }

    override fun buildMonthsList(
        focus: LocalDateFormatter,
        onClickCalendarPath: ((data: CalendarPathItem.Data) -> Unit)?
    ): List<CalendarPathItem.State> {
        return Month.entries.toTypedArray().mapIndexed { index, month ->
            CalendarPathItem.State(
                id = "${month}${index}",
                text = month.name,
                data = CalendarPathItem.Data.MonthCondition(month),
                isFocus = month == focus.month,
                onClick = onClickCalendarPath
            )
        }
    }

    private companion object {
        const val COUNT_YEARS = 5
    }
}