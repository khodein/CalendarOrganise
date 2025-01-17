package ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import androidx.annotation.ColorInt
import kotlinx.datetime.DayOfWeek
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.res.R as resR

class CalendarDaysOfWeekDelegateViewImpl(
    params: CalendarDaysOfWeekDelegateView.Params,
    context: Context,
) : CalendarDaysOfWeekDelegateView {

    private val daysOfWeek = DayOfWeek.entries.map(::mapDayOfWeek)

    @ColorInt
    private val textColorInt = params.textColor.getColor(context)
    private val textSize = params.textSize.value.toFloat()

    private val textPaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        color = textColorInt
        typeface = context.getFont(resR.font.regular)
        textSize = this@CalendarDaysOfWeekDelegateViewImpl.textSize
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }

    private val width: Float = params.width.toFloat()
    private val height: Float
        get() {
            val fontMetrics = textPaint.fontMetrics
            return (fontMetrics.bottom - fontMetrics.top)
        }

    private val stepWidth: Float = DimensionValue.Dp(25).value.toFloat()
    private val stepsWidth: Float
        get() = stepWidth * 6f

    private val textWidth: Float = textPaint.measureText(daysOfWeek.first())
    private val textsWidth: Float
        get() = textWidth * 7f

    private val startX: Float
        get() {
            val contentWidth = textsWidth + stepsWidth
            val paddingLeftOrRight = (width - contentWidth) / 2f
            return paddingLeftOrRight + (textWidth / 2f)
        }

    private val startY: Float
        get() = height - (textPaint.textSize / 2f)


    override fun getHeight(): Int {
        return height.toInt()
    }

    private val indentXtoX: Float = stepWidth + textWidth

    override fun drawDaysOfWeek(canvas: Canvas) {
        var x = startX
        val y = startY

        daysOfWeek.forEach { text ->
            canvas.drawText(text, x, y, textPaint)
            x += indentXtoX
        }
    }

    private fun mapDayOfWeek(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.name.substring(0, 3).lowercase().replaceFirstChar { it.titlecase() }
    }
}