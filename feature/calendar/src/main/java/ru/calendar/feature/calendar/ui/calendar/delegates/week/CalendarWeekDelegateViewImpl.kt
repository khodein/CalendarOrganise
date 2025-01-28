package ru.calendar.feature.calendar.ui.calendar.delegates.week

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarDay
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType
import ru.calendar.feature.calendar.ui.calendar.delegates.base.BaseCalendarDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

class CalendarWeekDelegateViewImpl(
    provider: CalendarProvider,
) : BaseCalendarDelegateView(
    provider = provider,
), CalendarWeekDelegateView {

    override var days: List<CalendarDay> = emptyList()

    private var month: Month = LocalDateFormatter.nowInSystemDefault().month
    override val type: CalendarType = CalendarType.WEEK

    override fun update(
        startDayOfWeek: LocalDateFormatter,
        month: Month,
        focus: LocalDateFormatter?,
        count: Int,
        params: CalendarParams?,
    ) {
        this.date = startDayOfWeek
        this.month = month
        this.focus = focus
        this.count = count
        this.params = params

        params ?: return

        updateParams(params)

        var date = startDayOfWeek

        var x = startX
        val y = startY

        days = buildList(WEEK_COUNT) {
            repeat(WEEK_COUNT) { count ->
                val isDayBefore = date.month > month
                val isDayAfter = date.month < month

                calendarDelegateMapper.mapDay(
                    x = x,
                    y = y,
                    focusDate = focus,
                    date = date,
                    isDaysAfter = isDayAfter,
                    isDaysBefore = isDayBefore,
                    count = count,
                    cellHeight = params.cell.height,
                    params = params
                ).let(::add)

                date = date.plusDays(1)
                x += indentXtoX
            }
        }
    }

    private fun updateParams(params: CalendarParams) {
        textPaint.apply {
            isAntiAlias = true
            isDither = true
            typeface = params.text.typeface
            textSize = params.text.textSize
            baselineShift = (textSize / 2 - descent()).toInt()
            textAlign = Paint.Align.CENTER
        }

        backgroundPaint.apply {
            isAntiAlias = true
            color = params.backgroundCell.emptyColorInt
        }

        focusRadius = params.focusRadius.end
        focusRadiusEnd = params.focusRadius.end
        focusRadiusStart = params.focusRadius.start

        todayRadius = params.todayRadius

        val stepWidth: Float = params.step.width
        val stepsWidth: Float = stepWidth * 6f

        val stepHeight: Float = 0f
        val stepsHeight: Float = 0f

        val cellWidth: Float = params.cell.width
        val cellsWidth: Float = cellWidth * 7f

        val cellHeight: Float = params.cell.height
        val cellsHeight: Float = cellHeight * 1f

        val width: Float = params.width.toFloat()
        height = cellsHeight + stepsHeight

        indentXtoX = stepWidth + cellWidth
        indentYtoY = stepHeight + cellHeight

        val contentWidth = cellsWidth + stepsWidth
        val paddingLeftOrRight = (width - contentWidth) / 2f

        startX = paddingLeftOrRight + (cellWidth / 2f)
        startY = (cellHeight / 2f) + params.startY
    }

    override fun onTouchEvent(event: MotionEvent?, onInvalidate: () -> Unit) {
        onClickDay(
            event = event,
            onUpdate = { date, focus ->
                update(
                    startDayOfWeek = date,
                    focus = focus,
                    month = month,
                    count = count,
                    params = params
                )
            },
            onInvalidate = onInvalidate
        )
    }

    override fun getHeight() = height.toInt()

    override fun draw(canvas: Canvas) {
        onDraw(canvas)
    }

    private companion object {
        const val WEEK_COUNT = 7
    }
}