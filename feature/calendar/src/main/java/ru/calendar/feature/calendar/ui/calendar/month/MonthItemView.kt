package ru.calendar.feature.calendar.ui.calendar.month

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateView

class MonthItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), MonthItem.View {

    private var daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView? = null
    private var monthDelegateView: CalendarMonthDelegateView? = null

    override fun bindState(state: MonthItem.State) {
        this.daysOfWeekDelegateView = state.daysOfWeekDelegateView
        this.monthDelegateView = state.monthDelegateView
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        daysOfWeekDelegateView?.draw(canvas)
        monthDelegateView?.draw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        monthDelegateView?.onTouchEvent(event)
        return true
    }

    override fun onUpdateView() {
        postInvalidate()
    }
}