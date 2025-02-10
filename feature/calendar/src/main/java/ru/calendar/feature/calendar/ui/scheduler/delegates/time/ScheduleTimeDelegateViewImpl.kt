package ru.calendar.feature.calendar.ui.scheduler.delegates.time

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.text.FontValue
import ru.calendar.feature.calendar.ui.scheduler.delegates.models.ScheduleTimeModel

class ScheduleTimeDelegateViewImpl(
    context: Context,
) : ScheduleTimeDelegateView {

    private var focusDay = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    private var rootWidth: Int = 0

    private val primaryColorInt by lazy { ColorValue.secondaryText.getColor(context) }

    private val scheduleTimeTextPaint = TextPaint().apply {
        isAntiAlias = true
        isDither = true
        color = primaryColorInt
        typeface = context.getFont(FontValue.REGULAR.fontResId)
        textSize = DimensionValue.SpToPx(10f).value.toFloat()
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.LEFT
    }

    private val scheduleTimeLinePaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = primaryColorInt
        strokeWidth = DimensionValue.Dp(1).value.toFloat()
    }

    private val scheduleTimeMap: HashMap<LocalDateFormatter, List<ScheduleTimeModel>> = HashMap()

    private var height = 0f

    override fun update(
        rootWidth: Int,
        focusDay: LocalDateFormatter,
        startX: DimensionValue.Dp,
        startY: DimensionValue.Dp
    ) {
        this.focusDay = focusDay.startOfTheDay()
        this.rootWidth = rootWidth

        scheduleTimeMap.getOrPut(focusDay) {
            getScheduleTimeList(
                startX = startX,
                startY = startY
            )
        }
    }
    override fun draw(canvas: Canvas, offsetX: Float, offsetY: Float) {
        scheduleTimeMap[focusDay]?.forEach { schedule ->
            canvas.drawText(
                schedule.format,
                schedule.time.x - offsetX,
                schedule.time.y - offsetY,
                scheduleTimeTextPaint
            )

            val lineY = schedule.line.y - offsetY
            canvas.drawLine(
                schedule.line.x - offsetX,
                lineY,
                rootWidth.toFloat(),
                lineY,
                scheduleTimeLinePaint
            )
        }
    }

    private fun getScheduleTimeList(
        startX: DimensionValue.Dp,
        startY: DimensionValue.Dp
    ): List<ScheduleTimeModel> {
        var timeStartOfTheDay = focusDay
        val startXValue = startX.value.toFloat()
        val startYValue = startY.value.toFloat()
        var y = startYValue
        val list = buildList(SCHEDULE_TIME_LIST_LIMIT) {
            repeat(SCHEDULE_TIME_LIST_LIMIT) {
                val fontMetrics = scheduleTimeTextPaint.fontMetrics
                val textHeight = fontMetrics.descent - fontMetrics.ascent
                val verticalOffset = (textHeight / 2) - fontMetrics.descent

                val time = ScheduleTimeModel.Time(
                    x = startXValue,
                    y = y + verticalOffset,
                )
                val line = ScheduleTimeModel.Line(
                    x = startXValue + DimensionValue.Dp(65).value,
                    y = y,
                )
                add(
                    ScheduleTimeModel(
                        time = time,
                        line = line,
                        timeFormatter = timeStartOfTheDay,
                        format = timeStartOfTheDay.getHHmmA(),
                    )
                )
                y += startYValue
                timeStartOfTheDay = timeStartOfTheDay.plusHour(1)
            }
        }
        height = y

        return list
    }

    override fun getHeight(): Float = height

    private companion object {
        const val SCHEDULE_TIME_LIST_LIMIT = 24
    }
}