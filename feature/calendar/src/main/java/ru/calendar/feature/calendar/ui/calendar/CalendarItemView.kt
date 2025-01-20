package ru.calendar.feature.calendar.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
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

    private val calendarWidth: Int = screenWidth

    private var date: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault()
    private var focus: LocalDateFormatter? = null

    @ColorInt
    private val backgroundColorInt = ColorValue.white.getColor(context)

    private val stepWidth = DimensionValue.Dp(30).value.toFloat()
    private val stepHeight = DimensionValue.Dp(12).value.toFloat()

    private val cellWidth = DimensionValue.Dp(24).value.toFloat()
    private val cellHeight = DimensionValue.Dp(24).value.toFloat()

    private val indentDayOfWeekToDayOfMonth = DimensionValue.Dp(14).value

    private val containerPaddingBottom = DimensionValue.Dp(20).value
    private val containerRadius = DimensionValue.Dp(20)

    private val weekAdapter = RecyclerAdapter()
    private var weekList: List<WeekItem.State> = emptyList()
    private var lastCountWeekFocus: Int? = null

    private val daysOfWeekParams by lazy {
        CalendarDaysOfWeekParams(
            width = calendarWidth,
            text = CalendarDaysOfWeekParams.Text(
                size = DimensionValue.SpToPx(12f).value.toFloat(),
                colorInt = ColorValue.secondaryText.getColor(context),
                typeface = context.getFont(FontValue.REGULAR.fontResId)
            ),
            step = CalendarDaysOfWeekParams.Step(width = stepWidth),
            cell = CalendarDaysOfWeekParams.Cell(width = cellWidth),
        )
    }

    private val calendarParams by lazy {
        CalendarParams(
            width = calendarWidth,
            today = LocalDateFormatter.nowInSystemDefault().startOfTheDay(),
            startY = daysOfWeekDelegateView.getHeight() + indentDayOfWeekToDayOfMonth,
            todayRadius = DimensionValue.Dp(8).value.toFloat(),
            text = CalendarParams.Text(
                textSize = DimensionValue.SpToPx(14f).value.toFloat(),
                noInMonthColorInt = ColorValue.tetriaty.getColor(context),
                inActiveColorInt = ColorValue.black.getColor(context),
                todayColorInt = ColorValue.primary.getColor(context),
                focusColorInt = ColorValue.white.getColor(context),
                typeface = context.getFont(FontValue.BOLD.fontResId)
            ),
            step = CalendarParams.Step(
                width = stepWidth,
                height = stepHeight
            ),
            cell = CalendarParams.Cell(
                width = cellWidth,
                height = cellHeight,
            ),
            backgroundCell = CalendarParams.BackgroundCell(
                focusColorInt = ColorValue.primary.getColor(context),
                todayColorInt = ColorValue.primary30.getColor(context),
                emptyColorInt = ColorValue.transparent.getColor(context)
            ),
            focusRadius = CalendarParams.FocusRadius(
                start = DimensionValue.Dp(7).value.toFloat(),
                end = DimensionValue.Dp(14).value.toFloat()
            )
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

    private var monthState = MonthItem.State(
        id = "month_id",
        daysOfWeekDelegateView = daysOfWeekDelegateView,
        monthDelegateView = calendarMonthDelegateView
    )

    init {
        layoutParams = LayoutParams(
            calendarWidth,
            WRAP_CONTENT
        )

        binding.calendarItemContainer.run {
            setBackgroundColor(backgroundColorInt)
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
    }

    private fun setWeekAdapter() = with(binding.calendarItemWeek) {
        adapter = weekAdapter
        itemAnimator = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }

    private fun buildWeekList() {
        weekList = buildList<WeekItem.State>(COUNT_WEEK) {
            val startDayOfMonth = date.startDayOfMonth().startOfTheDay()
            val startDayOfMonthDayOfWeek = startDayOfMonth.dayOfWeek.value
            val numberOfDaysBeforeMonth = startDayOfMonthDayOfWeek - 1
            var startDayOfWeek = startDayOfMonth.minusDays(numberOfDaysBeforeMonth)

            repeat(COUNT_WEEK) { count ->
                val weekViewDelegate: CalendarWeekDelegateView = CalendarWeekDelegateViewImpl(
                    params = calendarParams,
                    provider = this@CalendarItemView
                )

                weekViewDelegate.update(
                    startDayOfWeek = startDayOfWeek,
                    month = date.month,
                    focus = focus,
                    count = count
                )

                if (startDayOfWeek == focus) {
                    lastCountWeekFocus = count
                }

                weekCalendarHeight =
                    daysOfWeekDelegateView.getHeight() + indentDayOfWeekToDayOfMonth + weekViewDelegate.getHeight()

                WeekItem.State(
                    id = count.toString(),
                    width = calendarWidth,
                    height = weekCalendarHeight,
                    startDayOfWeek = startDayOfWeek,
                    calendarDaysOfWeekDelegateView = daysOfWeekDelegateView,
                    calendarWeekDelegateView = weekViewDelegate,
                ).let(::add)

                startDayOfWeek = startDayOfWeek.plusWeek(1)
            }
        }

        weekAdapter.submitList(weekList)
    }

    override fun bindState(state: CalendarItem.State) {
        this.date = state.month
        this.focus = state.focus

        binding.calendarItemMonth.isVisible = state.isMonth
        binding.calendarItemWeek.isVisible = !state.isMonth
    }

    override fun onClickFocus(
        focus: LocalDateFormatter,
        type: CalendarType,
        count: Int
    ) {
        this.focus = focus
        val lastItem = lastCountWeekFocus?.let { lastCount ->
            val item = weekList[lastCount]
            item.calendarWeekDelegateView.update(
                startDayOfWeek = item.startDayOfWeek,
                focus = focus,
                month = date.month,
                count = lastCount
            )
            item
        }

        val newItem = weekList[count]
        newItem.calendarWeekDelegateView.update(
            startDayOfWeek = newItem.startDayOfWeek,
            focus = focus,
            month = date.month,
            count = count
        )

        weekList = weekList.toMutableList().apply {
            lastItem?.let { set(lastCountWeekFocus ?: 0, lastItem) }
            set(count, newItem)
        }

        weekAdapter.submitList(weekList)
        this.lastCountWeekFocus = count
    }

    private companion object {
        const val COUNT_WEEK = 6
    }
}