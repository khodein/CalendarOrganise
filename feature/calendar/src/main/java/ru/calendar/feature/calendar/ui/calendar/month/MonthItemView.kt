package ru.calendar.feature.calendar.ui.calendar.month

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.ext.screenWidth
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.feature.calendar.ui.calendar.delegates.BaseCalendarDelegate
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateViewImpl

class MonthItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), MonthItem.View, BaseCalendarDelegate.Provider {

    private val monthWidth: Int = screenWidth
    private var monthHeight: Int = 0

    @ColorInt
    private val backgroundColorInt = ColorValue.white.getColor(context)

    private var onClickFocus: ((focus: LocalDateFormatter) -> Unit)? = null

    private val indentDayOfWeekToDayOfMonth = DimensionValue.Dp(14).value
    private val paddingBottom = DimensionValue.Dp(20).value
    private val radiusBottom = DimensionValue.Dp(20)

    private val stepWidth = DimensionValue.Dp(30)
    private val cellWidth = DimensionValue.Dp(24)

    private val daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView =
        CalendarDaysOfWeekDelegateViewImpl(
            params = CalendarDaysOfWeekDelegateView.Params(
                width = monthWidth,
                textSize = DimensionValue.SpToPx(12f),
                stepWidth = stepWidth,
                cellWidth = cellWidth,
                textColor = ColorValue.secondaryText,
            ),
            context = context,
        )

    private val daysOfWeekHeight: Int
        get() = daysOfWeekDelegateView.getHeight()

    private val calendarMonthDelegateView: CalendarMonthDelegateView = CalendarMonthDelegateViewImpl(
        params = BaseCalendarDelegate.Params(
            width = monthWidth,
            textNoInMonthColor = ColorValue.tetriaty,
            textInActiveColor = ColorValue.black,
            textSize = DimensionValue.SpToPx(14f),
            textTodayColor = ColorValue.primary,
            textFocusColor = ColorValue.white,
            stepWidth = stepWidth,
            cellHeight = DimensionValue.Dp(24),
            cellWidth = cellWidth,
            startY = daysOfWeekHeight + indentDayOfWeekToDayOfMonth,
            textFocusBackgroundColor = ColorValue.primary,
            textTodayBackgroundColor = ColorValue.primary30
        ),
        provider = this,
        context = context
    )

    private val monthCalendarHeight: Int
        get() = calendarMonthDelegateView.getHeight()

    init {
        calendarMonthDelegateView.update(
            date = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
        )

        monthHeight =
            daysOfWeekHeight +
                    indentDayOfWeekToDayOfMonth +
                    monthCalendarHeight +
                    paddingBottom

        setBackgroundColor(backgroundColorInt)

        makeRound(
            RoundValue(
                mode = RoundModeEntity.BOTTOM,
                radius = radiusBottom
            )
        )
    }

    override fun getMonthHeight(): Int {
        return monthHeight
    }

    override fun bindState(state: MonthItem.State) {
        this.onClickFocus = state.onClickFocus

        calendarMonthDelegateView.update(
            date = state.date,
            focus = state.focus
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(monthWidth, monthHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        daysOfWeekDelegateView.draw(canvas)
        calendarMonthDelegateView.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        calendarMonthDelegateView.onTouchEvent(event)
        return true
    }

    override fun onClickFocus(focus: LocalDateFormatter) {
        onClickFocus?.invoke(focus)
    }

    override fun onUpdateView() {
        postInvalidate()
    }
}