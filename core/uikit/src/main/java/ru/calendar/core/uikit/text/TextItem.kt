package ru.calendar.core.uikit.text

import android.content.Context
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionRect
import ru.calendar.core.tools.size.SizeValue
import ru.calendar.core.tools.text.TextValue

class TextItem {

    data class State(
        val id: String,
        val value: TextValue,
        val sizeValue: SizeValue = SizeValue(),
        val padding: DimensionRect = DimensionRect.R_0_0_0_0,
        val margins: DimensionRect = DimensionRect.R_0_0_0_0,
        val background: ColorValue? = null,
    ) : RecyclerState {
        override val provideId: String = id
        override val viewType: Int = android.view.View.generateViewId()
        override fun getView(context: Context) = TextItemView(context)
    }
}