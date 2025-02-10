package ru.calendar.feature.calendar.ui.scheduler

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.util.AttributeSet
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.screenWidth
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.ui.scheduler.base.BaseScrollerItemView
import ru.calendar.feature.calendar.ui.scheduler.delegates.time.ScheduleTimeDelegateView
import ru.calendar.feature.calendar.ui.scheduler.delegates.time.ScheduleTimeDelegateViewImpl

class ScheduleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseScrollerItemView(context, attrs, defStyleAttr), ScheduleItem.View {

    private var focusDay = LocalDateFormatter.nowInSystemDefault().startOfTheDay()

    private val scheduleTimeDelegate: ScheduleTimeDelegateView by lazy {
        ScheduleTimeDelegateViewImpl(context = context)
    }

    override val contentHeight: Float by lazy { scheduleTimeDelegate.getHeight() }
    override val contentWidth: Float by lazy { screenWidth.toFloat() }

    init {
        build()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        scheduleTimeDelegate.draw(
            canvas = canvas,
            offsetX = offsetX,
            offsetY = offsetY,
        )
    }

    override fun bindState(state: ScheduleItem.State) {
        this.focusDay = state.focusDate
        buildSchedule()
        stopScroll()
        smoothScrollToTop()
    }

    private fun build() {
        buildSchedule()
    }

    private fun buildSchedule() {
        scheduleTimeDelegate.update(
            startX = DimensionValue.Dp(20),
            startY = DimensionValue.Dp(125),
            focusDay = focusDay,
            rootWidth = contentWidth.toInt()
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        build()
        stopScroll()
        restoreScrollPosition(saveScrollPosition())
    }
}