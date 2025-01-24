package ru.calendar.core.uikit.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.tools.ext.applyMargin
import ru.calendar.core.tools.ext.applyPadding
import ru.calendar.core.tools.ext.load
import ru.calendar.core.tools.ext.setBackgroundView
import ru.calendar.core.tools.ext.setSizeValue

class TextItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), RecyclerItemView<TextItem.State> {

    init {
        includeFontPadding = false
    }

    override fun bindState(state: TextItem.State) {
        load(state.value)
        setSizeValue(state.sizeValue)
        setBackgroundView(state.background)
        applyMargin(state.margins)
        applyPadding(state.padding)
    }
}