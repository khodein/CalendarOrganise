package ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import androidx.annotation.ColorInt
import kotlinx.datetime.DayOfWeek
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.getColor

class CalendarDaysOfWeekDelegateViewImpl(

    private val context: Context,
) : CalendarDaysOfWeekDelegateView {

    private val daysOfWeek = DayOfWeek.entries.map(::mapDayOfWeek)

    @ColorInt
    private val daysOfWeekTextColorInt = ColorValue.secondaryText.getColor(context)
    private val daysOfWeeksTextSize = DimensionValue.SpToPx(12f).value.toFloat()

    private val dayOfWeekPaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        color = daysOfWeekTextColorInt
        textSize = daysOfWeeksTextSize
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }

    override fun drawDaysOfWeek(canvas: Canvas) {

    }

    private fun mapDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.name.substring(0, 3).uppercase()
    }
}