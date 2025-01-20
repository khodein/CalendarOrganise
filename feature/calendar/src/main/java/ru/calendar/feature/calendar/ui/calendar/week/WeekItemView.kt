package ru.calendar.feature.calendar.ui.calendar.week

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.setSizeValue
import ru.calendar.core.tools.size.SizeValue
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView

class WeekItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), WeekItem.View {

    private var daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView? = null
    private var calendarWeekDelegateView: CalendarWeekDelegateView? = null

    init {
        setSizeValue(
            SizeValue(
                width = DimensionValue.Dp(0),
                height = DimensionValue.Dp(0)
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        calendarWeekDelegateView?.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        daysOfWeekDelegateView?.draw(canvas)
        calendarWeekDelegateView?.draw(canvas)
    }

    override fun bindState(state: WeekItem.State) {
        setSizeValue(state.sizeValue)
        this.daysOfWeekDelegateView = state.calendarDaysOfWeekDelegateView
        this.calendarWeekDelegateView = state.calendarWeekDelegateView
        invalidate()
    }
}