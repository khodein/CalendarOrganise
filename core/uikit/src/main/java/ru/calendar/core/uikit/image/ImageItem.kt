package ru.calendar.core.uikit.image

import android.content.Context
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.tools.image.ImageValue
import ru.calendar.core.tools.size.SizeValue

class ImageItem {

    data class State(
        val id: String,
        val value: ImageValue,
        val sizeValue: SizeValue = SizeValue(),
    ) : RecyclerState {
        override val provideId: String = id
        override val viewType: Int = ImageItem::class.java.hashCode()
        override fun getView(context: Context) = ImageItemView(context)
    }
}