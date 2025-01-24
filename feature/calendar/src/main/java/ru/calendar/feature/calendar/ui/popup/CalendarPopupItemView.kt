package ru.calendar.feature.calendar.ui.popup

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import ru.calendar.core.recycler.adapter.RecyclerAdapter
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.makeRound
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.ext.setHeight
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.core.tools.round.RoundModeEntity
import ru.calendar.core.tools.round.RoundValue
import ru.calendar.feature.calendar.databinding.ViewCalendarPopupItemBinding
import ru.calendar.feature.calendar.ui.popup.mapper.CalendarPopupMapper
import ru.calendar.feature.calendar.ui.popup.mapper.CalendarPopupMapperImpl
import ru.calendar.feature.calendar.ui.popup.path.CalendarPathItem

class CalendarPopupItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CalendarPopupItem.View {

    private val binding = ViewCalendarPopupItemBinding.inflate(LayoutInflater.from(context), this)

    private var state: CalendarPopupItem.State? = null

    private val type: CalendarPopupItem.Type
        get() = state?.type ?: CalendarPopupItem.Type.MONTH_YEAR

    private val focus: LocalDateFormatter = LocalDateFormatter.nowInSystemDefault()

    private val calendarPopupMapper: CalendarPopupMapper by lazy { CalendarPopupMapperImpl() }

    private var yearsList: List<CalendarPathItem.State> = emptyList()
    private var monthsList: List<CalendarPathItem.State> = emptyList()

    private val yearsAdapter by lazy { RecyclerAdapter() }
    private val monthAdapter by lazy { RecyclerAdapter() }
    private val weekAdapter by lazy { RecyclerAdapter() }

    init {
        layoutParams = ViewGroup.LayoutParams(
            DimensionValue.MatchParent.value,
            DimensionValue.WrapContent.value
        )
        maxHeight = DimensionValue.Dp(30).value * 5
        setBackgroundView(ColorValue.white)
        elevation = 3f
        makeRound(
            RoundValue(
                mode = RoundModeEntity.ALL,
                radius = DimensionValue.Dp(6)
            )
        )
        setAdapter()
        build()
    }

    private val onYearsScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            updateHighlightedItem()
        }
    }

    private fun updateHighlightedItem() = with(binding.calendarYearsList) {
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
        val centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2
        if (centerPosition == 0 || centerPosition < 0) {
            return@with
        }
        val list = yearsList.mapIndexed { index, state ->
            state.copy(isFocus = index == centerPosition)
        }
        yearsAdapter.submitList(list)
    }

    private fun setAdapter() {
        binding.calendarYearsList.adapter = yearsAdapter
        binding.calendarMonthsList.adapter = monthAdapter
    }

    private fun build() {
        when (type) {
            CalendarPopupItem.Type.MONTH_YEAR -> {
                buildYearsList()
                buildMonthList()
            }

            CalendarPopupItem.Type.WEEK -> {

            }
        }
    }

    override fun bindState(state: CalendarPopupItem.State) {
        this.state = state

        build()
    }

    private fun onClickPath(data: CalendarPathItem.Data) {
        val localDateTime = when (data) {
            is CalendarPathItem.Data.MonthCondition -> {
                val month = data.month
                val year = focus.year
                val day = focus.dayOfYear
                LocalDateTime(
                    year,
                    month,
                    day,
                    0,
                    0,
                    0,
                    0,
                )
            }

            is CalendarPathItem.Data.YearCondition -> {
                val month = focus.month
                val day = focus.dayOfYear
                val year = data.year
                LocalDateTime(
                    year,
                    month,
                    day,
                    0,
                    0,
                    0,
                    0,
                )
            }
        }

        state?.date = LocalDateFormatter(localDateTime)
    }

    private fun buildYearsList() {
        yearsList = calendarPopupMapper.buildYearsList(
            focus = focus,
            onClickCalendarPath = ::onClickPath
        )

        yearsAdapter.submitList(yearsList)
    }

    private fun buildMonthList() {
        monthsList = calendarPopupMapper.buildMonthsList(
            focus = focus,
            onClickCalendarPath = ::onClickPath
        )

        monthAdapter.submitList(monthsList)
    }
}