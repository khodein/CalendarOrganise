package ru.calendar.feature.calendar.ui.calendar.mapper

import android.content.Context
import kotlinx.datetime.Month
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.text.FontValue
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarDaysOfWeekParams
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.model.WeekBuilderModel
import ru.calendar.feature.calendar.ui.calendar.week.WeekItem

class CalendarItemMapperImpl : CalendarItemMapper {

    override fun mapDayOfWeekParams(
        width: Int,
        stepWidth: Float,
        cellWidth: Float,
        context: Context,
    ): CalendarDaysOfWeekParams {
        return CalendarDaysOfWeekParams(
            width = width,
            text = CalendarDaysOfWeekParams.Text(
                size = DimensionValue.SpToPx(12f).value.toFloat(),
                colorInt = ColorValue.secondaryText.getColor(context),
                typeface = context.getFont(FontValue.REGULAR.fontResId)
            ),
            step = CalendarDaysOfWeekParams.Step(width = stepWidth),
            cell = CalendarDaysOfWeekParams.Cell(width = cellWidth),
        )
    }

    override fun mapCalendarParams(
        width: Int,
        startY: Int,
        stepWidth: Float,
        stepHeight: Float,
        cellWidth: Float,
        cellHeight: Float,
        context: Context,
    ): CalendarParams {
        val text = CalendarParams.Text(
            textSize = DimensionValue.SpToPx(14f).value.toFloat(),
            noInMonthColorInt = ColorValue.tetriaty.getColor(context),
            inActiveColorInt = ColorValue.black.getColor(context),
            todayColorInt = ColorValue.primary.getColor(context),
            focusColorInt = ColorValue.white.getColor(context),
            typeface = context.getFont(FontValue.BOLD.fontResId)
        )

        val step = CalendarParams.Step(
            width = stepWidth,
            height = stepHeight,
        )

        val cell = CalendarParams.Cell(
            width = cellWidth,
            height = cellHeight,
        )

        val backgroundCell = CalendarParams.BackgroundCell(
            focusColorInt = ColorValue.primary.getColor(context),
            todayColorInt = ColorValue.primary30.getColor(context),
            emptyColorInt = ColorValue.transparent.getColor(context)
        )

        val focusRadius = CalendarParams.FocusRadius(
            start = DimensionValue.Dp(7).value.toFloat(),
            end = DimensionValue.Dp(14).value.toFloat()
        )

        return CalendarParams(
            width = width,
            today = LocalDateFormatter.nowInSystemDefault().startOfTheDay(),
            startY = startY,
            todayRadius = DimensionValue.Dp(8).value.toFloat(),
            text = text,
            step = step,
            cell = cell,
            backgroundCell = backgroundCell,
            focusRadius = focusRadius
        )
    }

    override fun mapWeekItemByCount(
        weekList: List<WeekItem.State>,
        focus: LocalDateFormatter,
        month: Month,
        count: Int?
    ): WeekItem.State? {
        return count?.let {
            val item = weekList[count]
            item.calendarWeekDelegateView.update(
                startDayOfWeek = item.startDayOfWeek,
                focus = focus,
                month = month,
                count = count
            )
            item
        }
    }

    override fun mapWeekList(
        width: Int,
        heightWithoutWeek: Int,
        calendarParams: CalendarParams,
        date: LocalDateFormatter,
        focus: LocalDateFormatter?,
        daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView,
        provider: CalendarProvider
    ): WeekBuilderModel {
        var lastCountWeekFocus: Int = 0
        var weekCalendarHeight: Int = 0

        val weekList = buildList(COUNT_WEEK) {
            val startDayOfMonth = date.startDayOfMonth().startOfTheDay()
            val startDayOfMonthDayOfWeek = startDayOfMonth.dayOfWeek.value
            val numberOfDaysBeforeMonth = startDayOfMonthDayOfWeek - 1
            var startDayOfWeek = startDayOfMonth.minusDays(numberOfDaysBeforeMonth)

            repeat(COUNT_WEEK) { count ->
                val weekViewDelegate: CalendarWeekDelegateView = CalendarWeekDelegateViewImpl(
                    params = calendarParams,
                    provider = provider
                )

                weekViewDelegate.update(
                    startDayOfWeek = startDayOfWeek,
                    month = date.month,
                    focus = focus,
                    count = count
                )

                if (startDayOfWeek == focus) {
                    lastCountWeekFocus = count
                }

                val height = heightWithoutWeek + weekViewDelegate.getHeight()

                if (weekCalendarHeight == 0) {
                    weekCalendarHeight = height
                }

                WeekItem.State(
                    id = count.toString(),
                    width = width,
                    height = height,
                    startDayOfWeek = startDayOfWeek,
                    calendarDaysOfWeekDelegateView = daysOfWeekDelegateView,
                    calendarWeekDelegateView = weekViewDelegate,
                ).let(::add)

                startDayOfWeek = startDayOfWeek.plusWeek(1)
            }
        }

        return WeekBuilderModel(
            weekList = weekList,
            lastCountWeekFocus = lastCountWeekFocus,
            height = weekCalendarHeight
        )
    }

    companion object {
        private const val COUNT_WEEK = 6
    }
}