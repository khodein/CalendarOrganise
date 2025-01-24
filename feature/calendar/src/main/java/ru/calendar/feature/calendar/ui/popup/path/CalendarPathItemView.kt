package ru.calendar.feature.calendar.ui.popup.path

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.datetime.Month
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionRect
import ru.calendar.core.tools.dimension.DimensionValue
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.size.SizeValue
import ru.calendar.core.tools.text.FontValue
import ru.calendar.core.tools.text.TextStyleValue
import ru.calendar.core.tools.text.TextValue
import ru.calendar.core.uikit.text.TextItem
import ru.calendar.core.uikit.text.TextItemView

class CalendarPathItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CalendarPathItem.View,
    RecyclerItemView<CalendarPathItem.State> {

    private var textItemView: TextItemView? = null
    private var state: CalendarPathItem.State? = null

    init {
        layoutParams = LayoutParams(
            MATCH_PARENT,
            DimensionValue.Dp(30).value
        )

        applyPadding(
            DimensionRect(
                20,
                0,
                20,
                0
            )
        )

        setOnClickListener {
            state?.let { it.onClick?.invoke(it.data) }
        }
    }

    override fun bindState(state: CalendarPathItem.State) {
        this.state = state
        bindTextView(state.text)
        bindBackground(state.isFocus)
    }

    private fun bindBackground(isFocus: Boolean) {
        val colorValue = if (isFocus) {
            ColorValue.secondary
        } else {
            ColorValue.transparent
        }
        setBackgroundView(colorValue)
    }

    private fun bindTextView(text: String) {
        val textView = textItemView ?: getTextView(text)
        if (textItemView == null) {
            addView(textView)
        }
        textItemView = textView
    }

    private fun getTextView(text: String): TextItemView {
        val textStyle = TextValue(
            value = text,
            style = TextStyleValue.Custom(
                color = ColorValue.black,
                size = 12f,
                fontStyle = FontValue.BOLD
            )
        )
        return TextItemView(context).apply {
            layoutParams = LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT
            ).apply {
                textAlignment = TEXT_ALIGNMENT_CENTER
                gravity = Gravity.CENTER or Gravity.START
            }
            bindState(
                TextItem.State(
                    id = View.generateViewId().toString(),
                    value = textStyle,
                    sizeValue = SizeValue(
                        width = DimensionValue.WrapContent,
                        height = DimensionValue.WrapContent
                    )
                )
            )
        }
    }
}