package ru.calendar.core.uikit.button_icon

import android.content.Context
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.tools.color.ColorValue
import ru.calendar.core.tools.dimension.DimensionRect
import ru.calendar.core.tools.image.ImageValue

class ButtonIconItem {

    data class State(
        val id: String,
        val iconValue: ImageValue,
        val background: ColorValue = ColorValue.transparent,
        val ripple: ColorValue = ColorValue.primary30,
        val paddings: DimensionRect = DimensionRect.R_0_0_0_0,
        val onClick: (() -> Unit)? = null,
    ): RecyclerState {
        override val provideId: String = id
        override val viewType: Int = android.view.View.generateViewId()
        override fun getView(context: Context) = ButtonIconView(context)
    }
}