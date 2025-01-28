package ru.calendar.feature.calendar.ui.calendar.delegates.base

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.MotionEvent
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarDay
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType
import ru.calendar.feature.calendar.ui.calendar.delegates.mapper.CalendarDelegateMapper
import ru.calendar.feature.calendar.ui.calendar.delegates.mapper.CalendarDelegateMapperImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams

abstract class BaseCalendarDelegateView(
    private val provider: CalendarProvider,
) {
    protected var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    protected var params: CalendarParams? = null

    protected var count: Int = 0
    protected val calendarDelegateMapper: CalendarDelegateMapper = CalendarDelegateMapperImpl()

    private var focusAnimation: ValueAnimator? = null
    protected var focus: LocalDateFormatter? = null
    protected var focusRadius: Float = 0f
    protected var focusRadiusStart: Float = 0f
    protected var focusRadiusEnd: Float = 0f

    protected var todayRadius: Float = 0f

    abstract var days: List<CalendarDay>

    abstract val type: CalendarType

    protected var height: Float = 0f

    protected var indentXtoX: Float = 0f
    protected var indentYtoY: Float = 0f

    protected var startX: Float = 0f
    protected var startY: Float = 0f

    protected val textPaint: TextPaint = TextPaint()

    protected val backgroundPaint: Paint = Paint()

    private fun onAnimateFocus(touchDay: CalendarDay, onInvalidate: () -> Unit) {
        focusAnimation =
            ValueAnimator.ofFloat(focusRadiusStart, focusRadiusEnd).apply {
                duration = 100
                doOnStart {
                    focusRadius = focusRadiusStart
                }
                doOnEnd {
                    focusRadius = focusRadiusEnd
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
                day.coordinateTap.top < touchY && day.coordinateTap.bottom > touchY &&
                        day.coordinateTap.left < touchX && day.coordinateTap.right > touchX
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
            backgroundPaint.color = day.backgroundColorInt

            when {
                day.focus.isVisible -> {
                    canvas.drawCircle(
                        day.focus.x,
                        day.focus.y,
                        focusRadius,
                        backgroundPaint
                    )
                }

                day.today.isVisible -> {
                    canvas.drawRoundRect(
                        day.today.rectF,
                        todayRadius,
                        todayRadius,
                        backgroundPaint
                    )
                }
            }

            canvas.drawText(day.text, day.x, day.y, textPaint)
        }
    }
}