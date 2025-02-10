package ru.calendar.core.recycler.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.calendar.core.recycler.RecyclerItemView
import ru.calendar.core.recycler.RecyclerState

internal data class RecyclerViewHolder(
    private val view: View,
    val viewType: Int,
) : RecyclerView.ViewHolder(view) {

    fun bind(state: RecyclerState) {
        (view as? RecyclerItemView<RecyclerState>)?.bindState(state)
    }
}