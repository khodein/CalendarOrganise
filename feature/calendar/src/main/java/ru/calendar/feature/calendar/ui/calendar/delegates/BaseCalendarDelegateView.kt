package ru.calendar.feature.calendar.ui.calendar.delegates

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.calendar.core.tools.ext.dp
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

abstract class BaseCalendarDelegateView(
    private val provider: CalendarProvider,
    private val params: CalendarParams,
) {
    protected var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    protected var focus: LocalDateFormatter? = null
    protected var count: Int = 0

    private val todayRectF = RectF()

    private var focusAnimation: ValueAnimator? = null
    private var focusRadius: Float = 0f

    abstract var days: List<Day>

    abstract val type: CalendarType

    abstract val cellWidth: Float
    abstract val cellsWidth: Float

    abstract val cellHeight: Float
    abstract val cellsHeight: Float

    abstract val width: Float
    abstract val height: Float

    abstract val stepWidth: Float
    abstract val stepsWidth: Float

    abstract val stepHeight: Float
    abstract val stepsHeight: Float

    abstract val indentXtoX: Float
    abstract val indentYtoY: Float

    abstract val startX: Float
    abstract val startY: Float

    private val textPaint: TextPaint = TextPaint().apply {
        isAntiAlias = true
        isDither = true
        typeface = params.text.typeface
        textSize = params.text.textSize
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = params.backgroundCell.emptyColorInt
    }

    protected fun mapDay(
        count: Int,
        x: Float,
        y: Float,
        focus: LocalDateFormatter? = null,
        date: LocalDateFormatter,
        isDaysBefore: Boolean,
        isDaysAfter: Boolean,
    ): Day {
        val isToday: Boolean = date == params.today
        val isFocus: Boolean = date == focus

        val textColorInt = when {
            isFocus -> params.text.focusColorInt
            isToday -> params.text.todayColorInt
            isDaysAfter || isDaysBefore -> params.text.noInMonthColorInt
            else -> params.text.inActiveColorInt
        }

        val backgroundColorInt = when {
            isFocus -> params.backgroundCell.focusColorInt
            isToday -> params.backgroundCell.todayColorInt
            else -> params.backgroundCell.emptyColorInt
        }

        if (isFocus) {
            focusRadius = params.focusRadius.end
        }

        val pathCellHeight = cellHeight / 2f
        val paddingTopAndBottom = 6.dp.toFloat()
        val paddingLeftAndRight = paddingTopAndBottom / 3f

        val left = x - pathCellHeight - paddingLeftAndRight
        val top = y - pathCellHeight - paddingTopAndBottom
        val right = x + pathCellHeight + paddingLeftAndRight
        val bottom = y + paddingTopAndBottom

        if (isToday) {
            todayRectF.set(
                left,
                top,
                right,
                bottom
            )
        }

        return Day(
            count = count,
            x = x,
            y = y,
            text = date.dayOfMonth.toString(),
            textColorInt = textColorInt,
            isFocus = isFocus,
            isToday = isToday,
            coordinate = Day.Coordinate(
                top = top - paddingTopAndBottom,
                right = right + paddingLeftAndRight * 2f,
                left = left - paddingLeftAndRight * 2f,
                bottom = bottom + paddingTopAndBottom,
            ),
            date = date,
            backgroundColorInt = backgroundColorInt
        )
    }

    private fun onAnimateFocus(touchDay: Day, onInvalidate: () -> Unit) {
        focusAnimation =
            ValueAnimator.ofFloat(params.focusRadius.start, params.focusRadius.end).apply {
                duration = 100
                doOnStart {
                    focusRadius = params.focusRadius.start
                }
                doOnEnd {
                    focusRadius = params.focusRadius.end
                    focusAnimation = null
                    val focusCount = when(type) {
                        CalendarType.WEEK -> count
                        CalendarType.MONTH -> touchDay.count
                    }
                    provider.onClickFocus(
                        focus = touchDay.date,
                        type = type,
                        count = focusCount,
                    )
                }
                addUpdateListener { animation ->
                    focusRadius = animation.animatedValue as Float
                    onInvalidate.invoke()
                }
                start()
            }
    }

    protected fun onClickDay(
        event: MotionEvent?,
        onUpdate: (
            date: LocalDateFormatter,
            focus: LocalDateFormatter,
        ) -> Unit,
        onInvalidate: () -> Unit
    ) {
        if (focusAnimation != null) {
            return
        }

        val touchX = event?.x ?: 0f
        val touchY = event?.y ?: 0f

        val day = if (event?.action == MotionEvent.ACTION_UP) {
            val touchDay = days.find { day ->
                day.coordinate.top < touchY && day.coordinate.bottom > touchY &&
                        day.coordinate.left < touchX && day.coordinate.right > touchX
            }

            when {
                touchDay?.date == this.focus -> null
                touchDay != null -> touchDay
                else -> null
            }
        } else {
            null
        }

        if (day != null) {
            onUpdate.invoke(
                date,
                day.date
            )
            onAnimateFocus(touchDay = day, onInvalidate = onInvalidate)
        }
    }

    protected open fun onDraw(canvas: Canvas) {
        days.forEach { day ->
            textPaint.color = day.textColorInt

            when {
                day.isFocus -> {
                    backgroundPaint.color = params.backgroundCell.focusColorInt
                    canvas.drawCircle(
                        day.x,
                        day.y - cellHeight / 4f,
                        focusRadius,
                        backgroundPaint
                    )
                }

                day.isToday -> {
                    backgroundPaint.color = params.backgroundCell.todayColorInt
                    canvas.drawRoundRect(
                        todayRectF,
                        params.todayRadius,
                        params.todayRadius,
                        backgroundPaint
                    )
                }
            }

            canvas.drawText(day.text, day.x, day.y, textPaint)
        }
    }

    data class Day(
        val count: Int,
        val x: Float,
        val y: Float,
        val text: String,
        val date: LocalDateFormatter,
        val isFocus: Boolean,
        val isToday: Boolean,
        val coordinate: Coordinate,
        @ColorInt val textColorInt: Int,
        @ColorInt val backgroundColorInt: Int
    ) {
        data class Coordinate(
            val top: Float,
            val bottom: Float,
            val left: Float,
            val right: Float,
        )
    }
}