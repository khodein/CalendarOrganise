package ru.calendar.feature.calendar.ui.calendar.delegates.mapper

import android.graphics.RectF
import ru.calendar.core.tools.ext.dp
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarDay
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

class CalendarDelegateMapperImpl : CalendarDelegateMapper {
    override fun mapDay(
        count: Int,
        x: Float,
        y: Float,
        focusDate: LocalDateFormatter?,
        date: LocalDateFormatter,
        isDaysBefore: Boolean,
        isDaysAfter: Boolean,
        cellHeight: Float,
        params: CalendarParams
    ): CalendarDay {
        val isToday: Boolean = date == params.today
        val isFocus: Boolean = date == focusDate

        val textColorInt = when {
            isFocus -> params.text.focusColorInt
            isToday -> params.text.todayColorInt
            isDaysAfter || isDaysBefore -> params.text.noInMonthColorInt
            else -> params.text.inActiveColorInt
        }

        val backgroundColorInt = when {
            isFocus -> params.backgroundCell.focusColorInt
            isToday -> params.backgroundCell.todayColorInt
            else -> params.backgroundCell.emptyColorInt
        }

        val pathCellHeight = cellHeight / 2f
        val paddingTopAndBottom = 6.dp.toFloat()
        val paddingLeftAndRight = paddingTopAndBottom / 3f

        val left = x - pathCellHeight - paddingLeftAndRight
        val top = y - pathCellHeight - paddingTopAndBottom
        val right = x + pathCellHeight + paddingLeftAndRight
        val bottom = y + paddingTopAndBottom

        val todayRectF = RectF(
            left,
            top,
            right,
            bottom
        )

        val today = CalendarDay.Today(
            isVisible = isToday,
            rectF = todayRectF,
        )

        val focus = CalendarDay.Focus(
            x = x,
            y = y - cellHeight / 4f,
            isVisible = isFocus
        )

        val coordinateTap = CalendarDay.CoordinateTap(
            top = top - paddingTopAndBottom,
            right = right + paddingLeftAndRight * 2f,
            left = left - paddingLeftAndRight * 2f,
            bottom = bottom + paddingTopAndBottom,
        )

        return CalendarDay(
            count = count,
            x = x,
            y = y,
            textColorInt = textColorInt,
            focus = focus,
            today = today,
            coordinateTap = coordinateTap,
            date = date,
            backgroundColorInt = backgroundColorInt
        )
    }
}