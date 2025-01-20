package ru.calendar.feature.calendar.ui.calendar.week

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.tools.ext.setSize
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView

class WeekItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), WeekItem.View, RecyclerItemView<WeekItem.State> {

    private var daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView? = null
    private var weekDelegateView: CalendarWeekDelegateView? = null

    override fun bindState(state: WeekItem.State) {
        layoutParams = LayoutParams(
            state.width,
            state.height
        )
        this.daysOfWeekDelegateView = state.calendarDaysOfWeekDelegateView
        this.weekDelegateView = state.calendarWeekDelegateView
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        weekDelegateView?.onTouchEvent(
            event = event,
            onInvalidate = ::postInvalidate
        )
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        daysOfWeekDelegateView?.draw(canvas)
        weekDelegateView?.draw(canvas)
    }
}