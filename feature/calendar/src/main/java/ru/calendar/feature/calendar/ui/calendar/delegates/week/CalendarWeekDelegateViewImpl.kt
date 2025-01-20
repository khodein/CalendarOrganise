package ru.calendar.feature.calendar.ui.calendar.delegates.week

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import kotlinx.datetime.Month
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.day.Day
import ru.calendar.feature.calendar.ui.calendar.delegates.BaseCalendarDelegate

class CalendarWeekDelegateViewImpl(
    private val params: Params,
    provider: Provider,
    context: Context,
) : BaseCalendarDelegate(
    params = params,
    provider = provider,
    context = context,
), CalendarWeekDelegateView {

    override var days: List<Day> = emptyList()

    private var month: Month = LocalDateFormatter.nowInSystemDefault().month

    override val stepWidth: Float = params.stepWidth.value.toFloat()
    override val stepsWidth: Float
        get() = stepWidth * 6f

    override val stepHeight: Float = 0f
    override val stepsHeight: Float = 0f

    override val cellWidth: Float = params.cellWidth.value.toFloat()
    override val cellsWidth: Float
        get() = cellWidth * 7f

    override val cellHeight: Float = params.cellHeight.value.toFloat()
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
        focus: LocalDateFormatter?
    ) {
        this.date = startDayOfWeek
        this.month = month
        this.focus = focus

        var date = startDayOfWeek
        var x = startX
        val y = startY

        days = buildList<Day>(WEEK_COUNT) {
            val isDayBefore = date.month < month
            val isDayAfter = date.month > month

            repeat(WEEK_COUNT) {
                mapDay(
                    x = x,
                    y = y,
                    focus = focus,
                    date = date,
                    isDaysAfter = isDayAfter,
                    isDaysBefore = isDayBefore
                ).let(::add)

                date = date.plusDays(1)
                x += indentXtoX
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?) {
        onClickDay(
            event = event,
            onUpdate = { date, focus ->
                update(
                    startDayOfWeek = date,
                    focus = focus,
                    month = month
                )
            }
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