package ru.calendar.feature.calendar.ui.calendar.delegates

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.calendar.core.res.R
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.dp
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.calendar.day.Day

abstract class BaseCalendarDelegate(
    private val provider: Provider,
    params: Params,
    context: Context,
) {
    protected var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    protected var focus: LocalDateFormatter? = null

    private val today: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    protected val todayRadius: Float = DimensionValue.Dp(8).value.toFloat()
    protected val todayRectF = RectF()

    private var focusAnimation: ValueAnimator? = null
    protected var focusRadius: Float = 0f
    protected val endFocusRadius = DimensionValue.Dp(14).value.toFloat()

    abstract var days: List<Day>

    protected val textSize: Float = params.textSize.value.toFloat()

    @ColorInt
    private val textFocusColor: Int = params.textFocusColor.getColor(context)

    @ColorInt
    private val textTodayColor: Int = params.textTodayColor.getColor(context)

    @ColorInt
    private val textInActiveColor: Int = params.textInActiveColor.getColor(context)

    @ColorInt
    private val textNoInMonthColor: Int = params.textNoInMonthColor.getColor(context)

    @ColorInt
    protected val textTodayBackgroundColor: Int = params.textTodayBackgroundColor.getColor(context)

    @ColorInt
    protected val textFocusBackgroundColor: Int =  params.textFocusBackgroundColor.getColor(context)

    @ColorInt
    private val transparentColorInt: Int = ColorValue.transparent.getColor(context)

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
        typeface = context.getFont(R.font.bold)
        textSize = this@BaseCalendarDelegate.textSize
        baselineShift = (textSize / 2 - descent()).toInt()
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = textTodayBackgroundColor
    }

    protected fun mapDay(
        x: Float,
        y: Float,
        focus: LocalDateFormatter? = null,
        date: LocalDateFormatter,
        isDaysBefore: Boolean,
        isDaysAfter: Boolean,
    ): Day {
        val isToday: Boolean = date == today
        val isFocus: Boolean = date == focus?.startOfTheDay()

        val textColorInt = when {
            isFocus -> textFocusColor
            isToday -> textTodayColor
            isDaysAfter || isDaysBefore -> textNoInMonthColor
            else -> textInActiveColor
        }

        val backgroundColorInt = when {
            isFocus -> textFocusBackgroundColor
            isToday -> textTodayBackgroundColor
            else -> transparentColorInt
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

    private fun onAnimateFocus(touchDay: Day) {
        focusAnimation = ValueAnimator.ofFloat(0f, endFocusRadius).apply {
            duration = 200
            doOnStart {
                focusRadius = 0f
            }
            doOnEnd {
                focusRadius = endFocusRadius
                focusAnimation = null
                provider.onClickFocus(touchDay.date)
            }
            addUpdateListener { animation ->
                focusRadius = animation.animatedValue as Float
                provider.onUpdateView()
            }
            start()
        }
    }

    protected fun onClickDay(
        event: MotionEvent?,
        onUpdate: (
            date: LocalDateFormatter,
            focus: LocalDateFormatter,
        ) -> Unit
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
            onAnimateFocus(day)
        }
    }

    protected open fun onDraw(canvas: Canvas) {
        days.forEach { day ->
            textPaint.color = day.textColorInt

            when {
                day.isFocus -> {
                    backgroundPaint.color = textFocusBackgroundColor
                    canvas.drawCircle(
                        day.x,
                        day.y - cellHeight / 4f,
                        focusRadius,
                        backgroundPaint
                    )
                }

                day.isToday -> {
                    backgroundPaint.color = textTodayBackgroundColor
                    canvas.drawRoundRect(
                        todayRectF,
                        todayRadius,
                        todayRadius,
                        backgroundPaint
                    )
                }
            }

            canvas.drawText(day.text, day.x, day.y, textPaint)
        }
    }

    data class Params(
        val width: Int,
        val startY: Int,
        val cellWidth: DimensionValue.Dp,
        val cellHeight: DimensionValue.Dp,
        val stepWidth: DimensionValue.Dp,
        val textNoInMonthColor: ColorValue,
        val textInActiveColor: ColorValue,
        val textSize: DimensionValue.SpToPx,
        val textFocusColor: ColorValue,
        val textTodayColor: ColorValue,
        val textTodayBackgroundColor: ColorValue,
        val textFocusBackgroundColor: ColorValue,
    )

    interface Provider {
        fun onClickFocus(focus: LocalDateFormatter)
        fun onUpdateView()
    }
}