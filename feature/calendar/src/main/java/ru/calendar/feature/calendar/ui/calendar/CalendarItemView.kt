package ru.calendar.feature.calendar.ui.calendar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.recyclerview.widget.PagerSnapHelper
import ru.calendar.core.recycler.adapter.RecyclerAdapter
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.ext.screenWidth
import ru.calendar.core.tools.ext.setSize
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.feature.calendar.databinding.ViewCalendarItemBinding
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarDaysOfWeekParams
import ru.calendar.feature.calendar.ui.calendar.delegates.params.CalendarParams
import ru.calendar.feature.calendar.ui.calendar.mapper.CalendarItemMapper
import ru.calendar.feature.calendar.ui.calendar.mapper.CalendarItemMapperImpl
import ru.calendar.feature.calendar.ui.calendar.month.MonthItem
import ru.calendar.feature.calendar.ui.calendar.week.WeekItem

class CalendarItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CalendarItem.View, CalendarProvider {

    private val binding = ViewCalendarItemBinding.inflate(LayoutInflater.from(context), this)

    private var state: CalendarItem.State? = null

    private var calendarHeightAnimator: Animator? = null

    private val calendarItemMapper: CalendarItemMapper by lazy { CalendarItemMapperImpl(context) }

    private val calendarWidth: Int
        get() = screenWidth

    private val date: LocalDateFormatter
        get() = state?.date ?: LocalDateFormatter.nowInSystemDefault()

    private val focus: LocalDateFormatter?
        get() = state?.focus
    private var lastCountWeekFocus: Int? = null

    private val stepWidth by lazy { DimensionValue.Dp(30).value.toFloat() }
    private val stepHeight by lazy { DimensionValue.Dp(12).value.toFloat() }

    private val cellWidth by lazy { DimensionValue.Dp(24).value.toFloat() }
    private val cellHeight by lazy { DimensionValue.Dp(24).value.toFloat() }

    private val indentDayOfWeekToDayOfMonth by lazy { DimensionValue.Dp(14).value }

    private val containerPaddingBottom by lazy { DimensionValue.Dp(20).value }
    private val containerRadius by lazy { DimensionValue.Dp(20) }

    private val weekAdapter by lazy { RecyclerAdapter() }
    private var weekList: List<WeekItem.State> = emptyList()

    private val isMonth: Boolean
        get() = state?.isMonth ?: true

    private val daysOfWeekParams: CalendarDaysOfWeekParams
        get() = calendarItemMapper.mapDayOfWeekParams(
            width = calendarWidth,
            stepWidth = stepWidth,
            cellWidth = cellWidth,
        )

    private val daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView by lazy {
        CalendarDaysOfWeekDelegateViewImpl()
    }

    private val calendarParams: CalendarParams
        get() = calendarItemMapper.mapCalendarParams(
            width = calendarWidth,
            startY = daysOfWeekDelegateView.getHeight() + indentDayOfWeekToDayOfMonth,
            stepWidth = stepWidth,
            stepHeight = stepHeight,
            cellHeight = cellHeight,
            cellWidth = cellWidth,
        )

    private val calendarMonthDelegateView: CalendarMonthDelegateView by lazy {
        CalendarMonthDelegateViewImpl(provider = this)
    }

    private val monthCalendarHeight: Int
        get() {
            return daysOfWeekDelegateView.getHeight() +
                    indentDayOfWeekToDayOfMonth +
                    calendarMonthDelegateView.getHeight()
        }

    private var weekCalendarHeight: Int = 0

    init {
        layoutParams = LayoutParams(
            calendarWidth,
            WRAP_CONTENT
        )

        setWeekAdapter()

        setBackgroundColor(ColorValue.white.getColor(context))

        binding.calendarItemContainer.run {
            applyPadding(bottom = containerPaddingBottom)
            makeRound(
                RoundValue(
                    mode = RoundModeEntity.BOTTOM,
                    radius = containerRadius
                )
            )
        }

        buildCalendar()
        setCalendarHeight()
    }

    private fun buildCalendar() {
        updateDaysOfWeek()
        updateMonth()
        buildWeekList()
    }

    private fun setWeekAdapter() = with(binding.calendarItemWeek) {
        adapter = weekAdapter
        itemAnimator = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    private fun buildWeekList() {
        val weekBuilder = calendarItemMapper.mapWeekList(
            width = calendarWidth,
            date = date,
            focus = focus,
            heightWithoutWeek = daysOfWeekDelegateView.getHeight() + indentDayOfWeekToDayOfMonth,
            calendarParams = calendarParams,
            daysOfWeekDelegateView = daysOfWeekDelegateView,
            provider = this,
        )
        lastCountWeekFocus = weekBuilder.lastCountWeekFocus
        weekCalendarHeight = weekBuilder.height
        weekList = weekBuilder.weekList
        weekAdapter.submitList(weekList)
    }

    override fun bindState(state: CalendarItem.State) {
        this.state = state

        buildCalendar()

        if (state.isAnimate) {
            setAnimateCalendarHeight()
        } else {
            setCalendarHeight()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        buildCalendar()
        setCalendarHeight()
    }

    private fun setCalendarHeight() {
        binding.calendarItemMonth.isVisible = isMonth
        binding.calendarItemWeek.isVisible = !isMonth

        if (isMonth) {
            binding.calendarItemMonth.animate().alpha(1f)
            binding.calendarItemWeek.alpha = 0f
        } else {
            binding.calendarItemWeek.animate().alpha(1f)
            binding.calendarItemMonth.alpha = 0f
        }

        binding.calendarItemContainer.layoutParams = LayoutParams(
            calendarWidth,
            getCurrentHeight()
        )
        binding.calendarItemContainer.requestLayout()
    }

    private fun setAnimateCalendarHeight() {
        val startHeight = getStartAnimateHeightCalendar()
        val endHeight = getEndAnimateHeightCalendar()
        calendarHeightAnimator = ValueAnimator.ofInt(startHeight, endHeight).apply {
            duration = 200
            doOnStart {
                if (isMonth) {
                    binding.calendarItemWeek.animate().alpha(0f)
                    binding.calendarItemWeek.isVisible = false
                } else {
                    binding.calendarItemMonth.isVisible = false
                    binding.calendarItemMonth.animate().alpha(0f)
                }
            }
            doOnEnd {
                if (isMonth) {
                    binding.calendarItemMonth.isVisible = true
                    binding.calendarItemMonth.animate().alpha(1f)
                } else {
                    binding.calendarItemWeek.animate().alpha(1f)
                    binding.calendarItemWeek.isVisible = true
                }
                state?.isAnimate = false
                calendarHeightAnimator = null

                if (isMonth) {
                    updateMonth()
                }
            }
            addUpdateListener { animation ->
                binding.calendarItemContainer.layoutParams.height = animation.animatedValue as Int
                binding.calendarItemContainer.requestLayout()
            }
            start()
        }
    }

    private fun updateMonth() {
        calendarMonthDelegateView.update(
            date = date,
            focus = focus,
            params = calendarParams
        )

        binding.calendarItemMonth.setSize(
            width = calendarWidth,
            height = monthCalendarHeight
        )

        binding.calendarItemMonth.bindState(
            MonthItem.State(
                id = "month_id",
                daysOfWeekDelegateView = daysOfWeekDelegateView,
                monthDelegateView = calendarMonthDelegateView
            )
        )
    }

    private fun updateDaysOfWeek() {
        daysOfWeekDelegateView.update(daysOfWeekParams)
    }

    override fun onClickFocus(
        focus: LocalDateFormatter,
        type: CalendarType,
        count: Int
    ) {
        this.state?.focus = focus

        updateDaysOfWeek()

        val lastItem = calendarItemMapper.mapWeekItemByCount(
            weekList = weekList,
            focus = focus,
            month = date.month,
            count = lastCountWeekFocus,
            calendarParams = calendarParams,
        )

        val newItem = calendarItemMapper.mapWeekItemByCount(
            weekList = weekList,
            focus = focus,
            month = date.month,
            count = count,
            calendarParams = calendarParams,
        )

        weekList = weekList.toMutableList().apply {
            lastItem?.let { set(lastCountWeekFocus ?: 0, lastItem) }
            newItem?.let { set(count, it) }
        }

        weekAdapter.submitList(weekList)

        scrollToPosition(count)

        updateMonth()

        this.lastCountWeekFocus = count

        state?.onClickFocus?.invoke(focus)
    }

    private fun scrollToPosition(position: Int) {
        binding.calendarItemWeek.post {
            binding.calendarItemWeek.scrollToPosition(position)
        }
    }

    private fun getStartAnimateHeightCalendar(): Int {
        return if (isMonth) {
            weekCalendarHeight
        } else {
            monthCalendarHeight
        } + containerPaddingBottom
    }

    private fun getEndAnimateHeightCalendar(): Int {
        return if (isMonth) {
            monthCalendarHeight
        } else {
            weekCalendarHeight
        } + containerPaddingBottom
    }

    private fun getCurrentHeight(): Int {
        return if (isMonth) {
            monthCalendarHeight
        } else {
            weekCalendarHeight
        } + containerPaddingBottom
    }
}