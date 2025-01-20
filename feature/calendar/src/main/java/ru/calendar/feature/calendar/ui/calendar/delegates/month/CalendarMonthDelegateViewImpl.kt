package ru.calendar.feature.calendar.ui.calendar.delegates.month

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.day.Day
import ru.calendar.feature.calendar.ui.calendar.delegates.BaseCalendarDelegate

class CalendarMonthDelegateViewImpl(
    private val params: Params,
    private val provider: Provider,
    context: Context,
) : BaseCalendarDelegate(
    params = params,
    provider = provider,
    context = context,
), CalendarMonthDelegateView {

    override var days: List<Day> = emptyList()

    override val stepWidth: Float = params.stepWidth.value.toFloat()
    override val stepsWidth: Float
        get() = stepWidth * 6f

    override val stepHeight: Float = DimensionValue.Dp(12).value.toFloat()
    override val stepsHeight: Float
        get() = stepHeight * 5f

    override val cellWidth: Float = params.cellWidth.value.toFloat()
    override val cellsWidth: Float
        get() = cellWidth * 7f

    override val cellHeight: Float = params.cellHeight.value.toFloat()
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

        fun setCount() {
            if (count == 6) {
                count = 0
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

    override fun onTouchEvent(event: MotionEvent?) {
        onClickDay(
            event = event,
            onUpdate = ::update
        )
    }

    override fun getHeight(): Int {
        return height.toInt()
    }

    override fun draw(canvas: Canvas) {
        onDraw(canvas)
    }
}