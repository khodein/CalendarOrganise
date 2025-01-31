package ru.calendar.feature.calendar.ui.date_carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.formatter.LocalDateFormatter
import ru.calendar.feature.calendar.databinding.ViewDateCarouselPickerBinding
import ru.calendar.feature.calendar.ui.date_carousel.mapper.DateCarouselPickerMapper
import ru.calendar.feature.calendar.ui.date_carousel.mapper.DateCarouselPickerMapperImpl
import ru.calendar.feature.calendar.ui.date_carousel.model.DatePickerBuilder

class DateCarouselPickerItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), DateCarouselPickerItem.View {

    private val binding = ViewDateCarouselPickerBinding.inflate(LayoutInflater.from(context), this)

    private var state: DateCarouselPickerItem.State? = null

    private val defaultFocus by lazy {
        LocalDateFormatter.nowInSystemDefault().startOfTheDay()
    }

    private val focus: LocalDateFormatter
        get() = state?.focus ?: defaultFocus

    private val dateCarouselPickerMapper: DateCarouselPickerMapper by lazy {
        DateCarouselPickerMapperImpl()
    }

    private var pick: DataPick? = null

    private val onYearValueChangedListener by lazy {
        NumberPicker.OnValueChangeListener { _, _, new ->
            pick = DataPick(
                year = yearBuilder?.list?.getOrNull(new)?.toInt() ?: focus.year,
                month = pick?.month ?: focus.month.name
            )
        }
    }

    private val onMonthValueChangeListener by lazy {
        NumberPicker.OnValueChangeListener { _, _, new ->
            pick = DataPick(
                year = pick?.year ?: focus.year,
                month = monthBuilder?.list?.getOrNull(new) ?: focus.month.name
            )
        }
    }

    private var yearBuilder: DatePickerBuilder? = null
    private var monthBuilder: DatePickerBuilder? = null

    init {
        layoutParams = LayoutParams(
            DimensionValue.Dp(200).value,
            DimensionValue.WrapContent.value
        )

        setBackgroundView(ColorValue.white)

        yearBuilder = dateCarouselPickerMapper.mapYearList(focus)
        monthBuilder = dateCarouselPickerMapper.mapMonthList(focus)

        setPickers()
    }

    override fun bindState(state: DateCarouselPickerItem.State) {
        this.state = state

        if (focus.year != defaultFocus.year) {
            yearBuilder = dateCarouselPickerMapper.mapYearList(focus)
            setYearsPicker()
        }

        if (focus.month != defaultFocus.month) {
            monthBuilder = dateCarouselPickerMapper.mapMonthList(focus)
            setMonthsPicker()
        }
    }

    private fun setPickers() {
        setYearsPicker()
        setMonthsPicker()
    }

    private fun setYearsPicker() = with(binding.yearPicker) {
        val builder = yearBuilder ?: return@with
        setPickerResolve(
            view = this,
            builder = builder
        )
        setOnValueChangedListener(null)
        setOnValueChangedListener(onYearValueChangedListener)
    }

    private fun setMonthsPicker() = with(binding.monthPicker) {
        val builder = monthBuilder ?: return@with
        setPickerResolve(
            view = this,
            builder = builder
        )
        setOnValueChangedListener(null)
        setOnValueChangedListener(onMonthValueChangeListener)
    }

    private fun setPickerResolve(
        view: NumberPicker,
        builder: DatePickerBuilder,
    ) {
        with(view) {
            minValue = 0
            maxValue = builder.list.size - 1
            wrapSelectorWheel = true
            displayedValues = builder.list
            value = builder.focusIndex
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pick?.let {
            state?.onChangeDate?.invoke(it.year, it.month)
        }
        binding.monthPicker.setOnValueChangedListener(null)
        binding.yearPicker.setOnValueChangedListener(null)
    }

    private class DataPick(
        val year: Int,
        val month: String,
    )
}