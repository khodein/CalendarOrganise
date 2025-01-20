package ru.calendar.feature.calendar.ui.calendar.delegates.month

import android.graphics.Canvas
import android.view.MotionEvent
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.CalendarParams
import ru.calendar.feature.calendar.ui.calendar.day.Day
import ru.calendar.feature.calendar.ui.calendar.delegates.BaseCalendarDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType

class CalendarMonthDelegateViewImpl(
    private val params: CalendarParams,
    provider: CalendarProvider,
) : BaseCalendarDelegateView(
    params = params,
    provider = provider,
), CalendarMonthDelegateView {

    override var days: List<Day> = emptyList()

    override val type: CalendarType = CalendarType.MONTH

    override val stepWidth: Float = params.step.width
    override val stepsWidth: Float
        get() = stepWidth * 6f

    override val stepHeight: Float = params.step.height
    override val stepsHeight: Float
        get() = stepHeight * 5f

    override val cellWidth: Float = params.cell.width
    override val cellsWidth: Float
        get() = cellWidth * 7f

    override val cellHeight: Float = params.cell.height
    override val cellsHeight: Float
        get() = cellHeight * 6f

    override val width: Float = params.width.toFloat()
    override val height: Float
        get() {
            return cellsHeight + stepsHeight
        }

    override val indentXtoX: Float = stepWidth + cellWidth
    override val indentYtoY: Float = stepHeight + cellHeight

    override val startX: Float
        get() {
            val contentWidth = cellsWidth + stepsWidth
            val paddingLeftOrRight = (width - contentWidth) / 2f
            return paddingLeftOrRight + (cellWidth / 2f)
        }
    override val startY: Float
        get() {
            return (cellHeight / 2f) + params.startY
        }

    override fun update(
        date: LocalDateFormatter,
        focus: LocalDateFormatter?,
    ) {
        this.date = date
        this.focus = focus

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
            buildList<Day>(numberOfDaysBeforeMonth) {
                repeat(numberOfDaysBeforeMonth) {
                    add(
                        mapDay(
                            count = weekCount,
                            x = x,
                            y = y,
                            focus = focus,
                            isDaysBefore = true,
                            isDaysAfter = false,
                            date = dayBeforeMonth
                        )
                    )
                    dayBeforeMonth = dayBeforeMonth.plusDays(1)
                    setCount()
                }
            }
        } else {
            emptyList()
        }

        val monthsList = buildList<Day>(monthDays) {
            var dayOfMonth = startDayOfMonth
            repeat(monthDays) {
                add(
                    mapDay(
                        count = weekCount,
                        x = x,
                        y = y,
                        focus = focus,
                        isDaysAfter = false,
                        isDaysBefore = false,
                        date = dayOfMonth
                    )
                )
                dayOfMonth = dayOfMonth.plusDays(1)
                setCount()
            }
        }

        val daysAfterMonthsList = if (numberOfDaysAfterMonth > 0) {
            buildList<Day>(numberOfDaysAfterMonth) {
                var dayAfterMonth = endDayOfMonth.plusDays(1)
                repeat(numberOfDaysAfterMonth) {
                    add(
                        mapDay(
                            count = weekCount,
                            x = x,
                            y = y,
                            focus = focus,
                            isDaysAfter = true,
                            isDaysBefore = false,
                            date = dayAfterMonth
                        )
                    )
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

    override fun onTouchEvent(event: MotionEvent?, onInvalidate: () -> Unit) {
        onClickDay(
            event = event,
            onUpdate = ::update,
            onInvalidate = onInvalidate
        )
    }

    override fun getHeight(): Int {
        return height.toInt()
    }

    override fun draw(canvas: Canvas) {
        onDraw(canvas)
    }
}