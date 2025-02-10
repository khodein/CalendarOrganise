package ru.calendar.feature.calendar.ui.calendar.delegates.month

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarDay
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType
import ru.calendar.feature.calendar.ui.calendar.delegates.base.BaseCalendarDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

class CalendarMonthDelegateViewImpl(
    provider: CalendarProvider,
) : BaseCalendarDelegateView(
    provider = provider,
), CalendarMonthDelegateView {

    override var days: List<CalendarDay> = emptyList()

    override val type: CalendarType = CalendarType.MONTH

    override fun update(
        date: LocalDateFormatter,
        focus: LocalDateFormatter?,
        params: CalendarParams?,
    ) {
        this.date = date
        this.focus = focus
        this.params = params

        params ?: return

        updateParams(params)

        val monthDays = date.monthDays

        val startDayOfMonth = date.startDayOfMonth().startOfTheDay()
        val startDayOfMonthDayOfWeek = startDayOfMonth.dayOfWeek.value
        val numberOfDaysBeforeMonth = startDayOfMonthDayOfWeek - 1

        val endDayOfMonth = date.endDayOfMonth().startOfTheDay()
        val endDayOfMonthDayOfWeek = endDayOfMonth.dayOfWeek.value
        val numberOfDaysAfterMonth = (7 - endDayOfMonthDayOfWeek) + 7

        var x: Float = startX
        var y: Float = startY
        var count = 0
        var weekCount = 0

        fun setCount() {
            if (count == 6) {
                count = 0
                weekCount ++
                x = startX
                y += indentYtoY
            } else {
                count++
                x += indentXtoX
            }
        }

        val daysBeforeMonthList = if (numberOfDaysBeforeMonth > 0) {
            var dayBeforeMonth = startDayOfMonth.minusDays(numberOfDaysBeforeMonth)
            buildList(numberOfDaysBeforeMonth) {
                repeat(numberOfDaysBeforeMonth) {
                    calendarDelegateMapper.mapDay(
                        count = weekCount,
                        x = x,
                        y = y,
                        focusDate = focus,
                        isDaysBefore = true,
                        isDaysAfter = false,
                        date = dayBeforeMonth,
                        params = params,
                        cellHeight = params.cell.height
                    ).let(::add)
                    dayBeforeMonth = dayBeforeMonth.plusDays(1)
                    setCount()
                }
            }
        } else {
            emptyList()
        }

        val monthsList = buildList(monthDays) {
            var dayOfMonth = startDayOfMonth
            repeat(monthDays) {
                calendarDelegateMapper.mapDay(
                    count = weekCount,
                    x = x,
                    y = y,
                    focusDate = focus,
                    isDaysAfter = false,
                    isDaysBefore = false,
                    date = dayOfMonth,
                    params = params,
                    cellHeight = params.cell.height
                ).let(::add)
                dayOfMonth = dayOfMonth.plusDays(1)
                setCount()
            }
        }

        val daysAfterMonthsList = if (numberOfDaysAfterMonth > 0) {
            buildList(numberOfDaysAfterMonth) {
                var dayAfterMonth = endDayOfMonth.plusDays(1)
                repeat(numberOfDaysAfterMonth) {
                    calendarDelegateMapper.mapDay(
                        count = weekCount,
                        x = x,
                        y = y,
                        focusDate = focus,
                        isDaysAfter = true,
                        isDaysBefore = false,
                        date = dayAfterMonth,
                        params = params,
                        cellHeight = params.cell.height
                    ).let(::add)
                    dayAfterMonth = dayAfterMonth.plusDays(1)
                    setCount()
                }
            }
        } else {
            emptyList()
        }

        days = buildList(numberOfDaysAfterMonth + numberOfDaysBeforeMonth + monthDays) {
            addAll(daysBeforeMonthList)
            addAll(monthsList)
            addAll(daysAfterMonthsList)
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

        val stepHeight: Float = params.step.height
        val stepsHeight: Float = stepHeight * 5f

        val cellWidth: Float = params.cell.width
        val cellsWidth: Float = cellWidth * 7f

        val cellHeight: Float = params.cell.height
        val cellsHeight: Float = cellHeight * 6f

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
                    date = date,
                    focus = focus,
                    params = params
                )
            },
            onInvalidate = onInvalidate
        )
    }

    override fun getHeight(): Int = height.toInt()

    override fun draw(canvas: Canvas) {
        onDraw(canvas)
    }
}