package ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import kotlinx.datetime.DayOfWeek
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarDaysOfWeekParams

class CalendarDaysOfWeekDelegateViewImpl : CalendarDaysOfWeekDelegateView {

    private val days: MutableList<Day> = mutableListOf()

    private val textPaint: TextPaint = TextPaint()

    private var height: Float = 0f

    private var indentXtoX: Float = 0f

    private var startX: Float = 0f
    private var startY: Float = 0f

    override fun getHeight(): Int = height.toInt()

    override fun draw(canvas: Canvas) {
        days.forEach { day ->
            canvas.drawText(day.text, day.x, day.y, textPaint)
        }
    }

    override fun update(params: CalendarDaysOfWeekParams) {
        updateParams(params)
        buildList()
    }

    private fun buildList() {
        var x = startX
        val y = startY

        days.clear()

        val list = buildList(COUNT_DAY_OF_WEEK) {
            DayOfWeek.entries.map(::mapDay).forEach { text ->
                Day(
                    x = x,
                    y = y,
                    text = text
                ).let(::add)
                x += indentXtoX
            }
        }

        days.addAll(list)
    }

    private fun updateParams(params: CalendarDaysOfWeekParams) {
        textPaint.apply {
            isAntiAlias = true
            isDither = true
            color = params.text.colorInt
            typeface = params.text.typeface
            textSize = params.text.size
            baselineShift = (textSize / 2 - descent()).toInt()
            textAlign = Paint.Align.CENTER
        }

        val width = params.width.toFloat()
        height = (textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent)

        val stepWidth = params.step.width
        val stepsWidth = stepWidth * 6f

        val cellWidth = params.cell.width
        val cellsWidth = cellWidth * 7f

        indentXtoX = cellWidth + stepWidth

        val contentWidth = cellsWidth + stepsWidth
        val paddingLeftOrRight = (width - contentWidth) / 2f

        startX = paddingLeftOrRight + (cellWidth / 2f)
        startY = textPaint.textSize
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