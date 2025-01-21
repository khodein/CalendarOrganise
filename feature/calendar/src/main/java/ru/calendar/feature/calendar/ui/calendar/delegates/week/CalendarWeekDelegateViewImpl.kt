package ru.calendar.feature.calendar.ui.calendar.delegates.week

import android.graphics.Canvas
import android.view.MotionEvent
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams
import ru.calendar.feature.calendar.ui.calendar.day.Day
import ru.calendar.feature.calendar.ui.calendar.delegates.BaseCalendarDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType

class CalendarWeekDelegateViewImpl(
    private val params: CalendarParams,
    provider: CalendarProvider,
) : BaseCalendarDelegateView(
    params = params,
    provider = provider,
), CalendarWeekDelegateView {

    override var days: List<Day> = emptyList()

    private var month: Month = LocalDateFormatter.nowInSystemDefault().month
    override val type: CalendarType = CalendarType.WEEK

    override val stepWidth: Float = params.step.width
    override val stepsWidth: Float
        get() = stepWidth * 6f

    override val stepHeight: Float = 0f
    override val stepsHeight: Float = 0f

    override val cellWidth: Float = params.cell.width
    override val cellsWidth: Float
        get() = cellWidth * 7f

    override val cellHeight: Float = params.cell.height
    override val cellsHeight: Float
        get() = cellHeight * 1f

    override val width: Float = params.width.toFloat()
    override val height: Float
        get() = cellsHeight + stepsHeight

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
        startDayOfWeek: LocalDateFormatter,
        month: Month,
        focus: LocalDateFormatter?,
        count: Int,
    ) {
        this.date = startDayOfWeek
        this.month = month
        this.focus = focus
        this.count = count

        var date = startDayOfWeek

        var x = startX
        val y = startY

        days = buildList<Day>(WEEK_COUNT) {
            repeat(WEEK_COUNT) { count ->
                val isDayBefore = date.month > month
                val isDayAfter = date.month < month

                mapDay(
                    x = x,
                    y = y,
                    focus = focus,
                    date = date,
                    isDaysAfter = isDayAfter,
                    isDaysBefore = isDayBefore,
                    count = count
                ).let(::add)

                date = date.plusDays(1)
                x += indentXtoX
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?, onInvalidate: () -> Unit) {
        onClickDay(
            event = event,
            onUpdate = { date, focus ->
                update(
                    startDayOfWeek = date,
                    focus = focus,
                    month = month,
                    count = count
                )
            },
            onInvalidate = onInvalidate
        )
    }

    override fun getHeight(): Int {
        return height.toInt()
    }

    override fun draw(canvas: Canvas) {
        onDraw(canvas)
    }

    private companion object {
        const val WEEK_COUNT = 7
    }
}