package ru.calendar.feature.calendar.ui.calendar

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
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
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.ext.screenWidth
import ru.calendar.core.tools.ext.setHeight
import ru.calendar.core.tools.ext.setSize
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.core.tools.text.FontValue
import ru.calendar.feature.calendar.databinding.ViewCalendarItemBinding
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarProvider
import ru.calendar.feature.calendar.ui.calendar.delegates.CalendarType
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.daysOfWeek.CalendarDaysOfWeekDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.month.CalendarMonthDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateView
import ru.calendar.feature.calendar.ui.calendar.delegates.week.CalendarWeekDelegateViewImpl
import ru.calendar.feature.calendar.ui.calendar.mapper.CalendarItemMapper
import ru.calendar.feature.calendar.ui.calendar.mapper.CalendarItemMapperImpl
import ru.calendar.feature.calendar.ui.calendar.month.MonthItem
import ru.calendar.feature.calendar.ui.calendar.week.WeekItem
import kotlin.math.max
import kotlin.math.min

class CalendarItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CalendarItem.View, CalendarProvider {

    private val binding = ViewCalendarItemBinding.inflate(LayoutInflater.from(context), this)

    private var state: CalendarItem.State? = null

    private var calendarHeightAnimator: Animator? = null

    private val calendarItemMapper: CalendarItemMapper by lazy { CalendarItemMapperImpl() }

    private val calendarWidth: Int by lazy { screenWidth }

    private val date: LocalDateFormatter
        get() = state?.date ?: LocalDateFormatter.nowInSystemDefault()

    private val focus: LocalDateFormatter?
        get() = state?.focus
    private var lastCountWeekFocus: Int? = null

    @get:ColorInt
    private val backgroundColorInt by lazy { ColorValue.white.getColor(context) }

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

    private val daysOfWeekParams by lazy {
        calendarItemMapper.mapDayOfWeekParams(
            width = calendarWidth,
            stepWidth = stepWidth,
            cellWidth = cellWidth,
            context = context,
        )
    }

    private val calendarParams by lazy {
        calendarItemMapper.mapCalendarParams(
            width = calendarWidth,
            startY = daysOfWeekDelegateView.getHeight() + indentDayOfWeekToDayOfMonth,
            stepWidth = stepWidth,
            stepHeight = stepHeight,
            cellHeight = cellHeight,
            cellWidth = cellWidth,
            context = context
        )
    }

    private val daysOfWeekDelegateView: CalendarDaysOfWeekDelegateView =
        CalendarDaysOfWeekDelegateViewImpl(
            params = daysOfWeekParams
        ).apply {
            update()
        }

    private val calendarMonthDelegateView: CalendarMonthDelegateView =
        CalendarMonthDelegateViewImpl(
            params = calendarParams,
            provider = this,
        ).apply {
            update(
                date = date,
                focus = null
            )
        }

    private val monthCalendarHeight: Int
        get() {
            return daysOfWeekDelegateView.getHeight() +
                    indentDayOfWeekToDayOfMonth +
                    calendarMonthDelegateView.getHeight()
        }

    private var weekCalendarHeight: Int = 0

    private val monthState by lazy {
        MonthItem.State(
            id = "month_id",
            daysOfWeekDelegateView = daysOfWeekDelegateView,
            monthDelegateView = calendarMonthDelegateView
        )
    }

    init {
        layoutParams = LayoutParams(
            calendarWidth,
            WRAP_CONTENT
        )

        setBackgroundColor(backgroundColorInt)

        binding.calendarItemContainer.run {
            applyPadding(bottom = containerPaddingBottom)
            makeRound(
                RoundValue(
                    mode = RoundModeEntity.BOTTOM,
                    radius = containerRadius
                )
            )
        }

        binding.calendarItemMonth.run {
            setSize(
                width = calendarWidth,
                height = monthCalendarHeight
            )
            bindState(monthState)
        }

        setWeekAdapter()
        buildWeekList()

        setCalendarHeight(isMonth)
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
        if (state.isAnimate) {
            setAnimateCalendarHeight(state.isMonth)
        } else {
            setCalendarHeight(state.isMonth)
            updateMonth(focus)
            buildWeekList()
        }
    }

    private fun setCalendarHeight(
        isMonth: Boolean
    ) {
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

    private fun setAnimateCalendarHeight(isMonth: Boolean) {
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
            }
            addUpdateListener { animation ->
                binding.calendarItemContainer.layoutParams.height = animation.animatedValue as Int
                binding.calendarItemContainer.requestLayout()
            }
            start()
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

    private fun updateWeekList(
        count: Int,
        focus: LocalDateFormatter,
    ) {
        val lastItem = calendarItemMapper.mapWeekItemByCount(
            weekList = weekList,
            focus = focus,
            month = date.month,
            count = lastCountWeekFocus
        )

        val newItem = calendarItemMapper.mapWeekItemByCount(
            weekList = weekList,
            focus = focus,
            month = date.month,
            count = count
        )

        weekList = weekList.toMutableList().apply {
            lastItem?.let { set(lastCountWeekFocus ?: 0, lastItem) }
            newItem?.let { set(count, it) }
        }

        weekAdapter.submitList(weekList)
    }

    private fun updateMonth(focus: LocalDateFormatter?) {
        calendarMonthDelegateView.update(
            date = date,
            focus = focus
        )
        binding.calendarItemMonth.bindState(monthState)
    }

    override fun onClickFocus(
        focus: LocalDateFormatter,
        type: CalendarType,
        count: Int
    ) {
        this.state?.focus = focus

        updateWeekList(
            count = count,
            focus = focus
        )

        updateMonth(focus)

        if (state?.isMonth == true) {
            binding.calendarItemWeek.post {
                binding.calendarItemWeek.scrollToPosition(count)
            }
        }

        this.lastCountWeekFocus = count

        state?.onClickFocus?.invoke(focus)
    }
}