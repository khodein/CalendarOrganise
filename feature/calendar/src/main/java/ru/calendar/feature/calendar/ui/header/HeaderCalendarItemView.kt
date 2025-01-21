package ru.calendar.feature.calendar.ui.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionRect
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.image.ImageValue
import ru.calendar.core.uikit.button_icon.ButtonIconItem
import ru.calendar.core.uikit.text.TextItem
import ru.calendar.feature.calendar.databinding.ViewHeaderCalendarBinding
import ru.calendar.core.res.R as resR

class HeaderCalendarItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), HeaderCalendarItem.View {

    private val binding = ViewHeaderCalendarBinding.inflate(LayoutInflater.from(context), this)
    private var state: HeaderCalendarItem.State? = null

    init {
        layoutParams = LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        )

        setBackgroundView(ColorValue.white)
        applyPadding(DimensionRect.R_24_20_24_20)
    }

    override fun bindState(state: HeaderCalendarItem.State) {
        this.state = state
        binding.headerCalendarAdded.bindState(
            ButtonIconItem.State(
                id = "headerCalendarAdded",
                iconValue = ImageValue(value = resR.drawable.ic_plus),
                paddings = DimensionRect(
                    left = 8,
                    right = 0,
                    top = 0,
                    bottom = 0
                ),
                onClick = state.onClickAdded
            )
        )
        binding.headerCalendarExcluded.bindState(
            ButtonIconItem.State(
                id = "headerCalendarExcluded",
                iconValue = ImageValue(value = resR.drawable.ic_excluded),
                onClick = state.onClickExcluded
            )
        )
        binding.headerCalendarMonth.text = state.monthCalendar
    }
}