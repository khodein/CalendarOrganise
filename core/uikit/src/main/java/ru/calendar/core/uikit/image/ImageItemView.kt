package ru.calendar.core.uikit.image

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.tools.ext.load
import ru.calendar.core.tools.ext.setSizeValue

class ImageItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), RecyclerItemView<ImageItem.State> {

    override fun bindState(state: ImageItem.State) {
        load(state.value)
        setSizeValue(state.sizeValue)
    }
}