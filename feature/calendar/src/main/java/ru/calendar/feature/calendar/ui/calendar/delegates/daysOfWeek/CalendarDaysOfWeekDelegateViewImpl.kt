package ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import kotlinx.datetime.DayOfWeek
import ru.calendar.feature.calendar.ui.calendar.CalendarDaysOfWeekParams

class CalendarDaysOfWeekDelegateViewImpl(
    params: CalendarDaysOfWeekParams,
) : CalendarDaysOfWeekDelegateView {

    private var days: List<Day> = emptyList()

    private val textPaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        isDither = true
        color = params.text.colorInt
        typeface = params.text.typeface
        textSize = params.text.size
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }

    private val width: Float = params.width.toFloat()
    private val height: Float
        get() {
            val fontMetrics = textPaint.fontMetrics
            return (fontMetrics.descent - fontMetrics.ascent)
        }

    private val stepWidth: Float = params.step.width
    private val stepsWidth: Float = stepWidth * 6f

    private val cellWidth: Float = params.cell.width
    private val cellsWidth: Float = cellWidth * 7f

    private val indentXtoX: Float = cellWidth + stepWidth

    private val startX: Float
        get() {
            val contentWidth = cellsWidth + stepsWidth
            val paddingLeftOrRight = (width - contentWidth) / 2f
            return paddingLeftOrRight + (cellWidth / 2f)
        }
    private val startY: Float = textPaint.textSize

    override fun getHeight(): Int {
        return height.toInt()
    }

    override fun draw(canvas: Canvas) {
        days.forEach { day ->
            canvas.drawText(day.text, day.x, day.y, textPaint)
        }
    }

    override fun update() {
        var x = startX
        val y = startY

        days = buildList(COUNT_DAY_OF_WEEK) {
            DayOfWeek.entries.map(::mapDay).forEach { text ->
                Day(
                    x = x,
                    y = y,
                    text = text
                ).let(::add)
                x += indentXtoX
            }
        }
    }

    private fun mapDay(dayOfWeek: DayOfWeek): String {
        return dayOfWeek.name.substring(0, 3).lowercase().replaceFirstChar { it.titlecase() }
    }

    private data class Day(
        val x: Float,
        val y: Float,
        val text: String,
    )

    private companion object {
        const val COUNT_DAY_OF_WEEK = 7
    }
}