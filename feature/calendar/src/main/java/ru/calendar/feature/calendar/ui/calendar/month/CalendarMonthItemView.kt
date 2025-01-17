package ru.calendar.feature.calendar.ui.calendar.month

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.screenWidth
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.days_of_week.CalendarDaysOfWeekDelegateViewImpl

class CalendarMonthItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), CalendarMonthItem.View {

    private val monthWidth = screenWidth
    private var monthHeight: Int = 0

    private val daysOfWeekParams by lazy {
        CalendarDaysOfWeekDelegateView.Params(
            width = monthWidth,
            textSize = DimensionValue.SpToPx(12f),
            textColor = ColorValue.secondaryText,
        )
    }

    private val daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView =
        CalendarDaysOfWeekDelegateViewImpl(
            params = daysOfWeekParams,
            context = context,
        )

    init {
        monthHeight = daysOfWeekDelegateView.getHeight()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(monthWidth, monthHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        daysOfWeekDelegateView.drawDaysOfWeek(canvas)
    }
}