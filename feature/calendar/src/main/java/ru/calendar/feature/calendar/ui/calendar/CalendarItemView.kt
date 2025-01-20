package ru.calendar.feature.calendar.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.getColor
import ru.calendar.core.tools.ext.getFont
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.ext.screenWidth
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
import ru.calendar.feature.calendar.ui.calendar.month.MonthItem

class CalendarItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CalendarItem.View, CalendarProvider {

    private val binding = ViewCalendarItemBinding.inflate(LayoutInflater.from(context), this)

    private val calendarWidth: Int = screenWidth

    private var month: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault().startOfTheDay()
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
                date = month,
                focus = null
            )
        }

    private val monthCalendarHeight: Int
        get() {
            return daysOfWeekDelegateView.getHeight() +
                    indentDayOfWeekToDayOfMonth +
                    calendarMonthDelegateView.getHeight()
        }

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
    }

    override fun bindState(state: CalendarItem.State) {
        this.month = state.month
        this.focus = state.focus

        binding.calendarItemMonth.isVisible = state.isMonth
        binding.calendarItemWeek.isVisible = !state.isMonth
    }

    override fun onClickFocus(focus: LocalDateFormatter) {

    }

    override fun onUpdateView(type: CalendarType) {
        when (type) {
            CalendarType.MONTH -> binding.calendarItemMonth.onUpdateView()
            CalendarType.WEEK -> {

            }
        }
    }
}